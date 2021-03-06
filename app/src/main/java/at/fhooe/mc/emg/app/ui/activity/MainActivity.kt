package at.fhooe.mc.emg.app.ui.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.app.core.AndroidEmgPresenter
import at.fhooe.mc.emg.app.dagger.AppComponent
import at.fhooe.mc.emg.app.ui.activity.core.BaseActivity
import at.fhooe.mc.emg.app.ui.fragment.MainFragment
import at.fhooe.mc.emg.app.ui.fragment.dialog.TextEnterDialogFragment
import at.fhooe.mc.emg.app.view.AndroidEmgView
import at.fhooe.mc.emg.app.view.OnRenderViewReadyListener
import at.fhooe.mc.emg.clientdriver.EmgClientDriver
import at.fhooe.mc.emg.core.EmgPresenter
import at.fhooe.mc.emg.core.analysis.FrequencyAnalysisMethod
import at.fhooe.mc.emg.core.filter.Filter
import at.fhooe.mc.emg.core.storage.CsvEmgDataStorage
import at.fhooe.mc.emg.core.tool.Tool
import at.fhooe.mc.emg.core.util.EmgConfig
import at.fhooe.mc.emg.core.view.EmgViewCallback
import at.fhooe.mc.emg.core.view.VisualView
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class MainActivity : BaseActivity(), AndroidEmgView<View>, OnRenderViewReadyListener {

    @Inject
    protected lateinit var emgPresenter: AndroidEmgPresenter

    private lateinit var viewCallback: EmgViewCallback

    private lateinit var config: EmgConfig

    private lateinit var visualView: VisualView<View>

    private var renderView: AndroidEmgView<View>? = null

    private var menuItemConnect: MenuItem? = null
    private var menuItemDisconnect: MenuItem? = null
    private var menuItemSamplingFrequency: MenuItem? = null
    private var menuItemDisableVisualView: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showRenderView(savedInstanceState)
    }

    override fun injectToGraph(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        menuItemConnect = menu?.findItem(R.id.menu_main_connect)
        menuItemDisconnect = menu?.findItem(R.id.menu_main_disconnect)
        menuItemSamplingFrequency = menu?.findItem(R.id.menu_main_sample_frequency)
        menuItemDisableVisualView = menu?.findItem(R.id.menu_main_disable_visual_view)

        // This is the point where all necessary controls are initialized
        attachEmgView()

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {

            R.id.menu_main_reset -> reset()
            R.id.menu_main_connect -> {
                viewCallback.connectToClient()
                lockOrientation(true)
            }
            R.id.menu_main_disconnect -> {
                disconnectFromDevice()
                lockOrientation(false)
            }
            R.id.menu_main_disable_visual_view -> {
                // Inverse logic, because click has to be performed manually by the code
                val isChecked = menuItemDisableVisualView?.isChecked!!
                menuItemDisableVisualView?.isChecked = !isChecked
                viewCallback.setVisualViewEnabled(!isChecked)
            }
            R.id.menu_main_sample_frequency -> showSamplingFrequencyDialog()
            R.id.menu_main_export -> showExportDialogFragment()
            R.id.menu_main_settings -> showSettings()
        }
        return super.onOptionsItemSelected(item)
    }

    // --------------------------------------------------------------------

    override fun setVisualView(view: VisualView<View>) {
        visualView = view
        renderView?.setVisualView(view)
    }

    override fun exposeRawClientDataObservable(observable: Observable<String>) {
        renderView?.exposeRawClientDataObservable(observable)
    }

    override fun reset() {
        Completable.fromAction {
            renderView?.reset()
        }.subscribeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    override fun lockDeviceControls(isLocked: Boolean) {

        // UiThread is needed, because connection error occurs on different thread
        Completable.fromAction {
            renderView?.lockDeviceControls(isLocked)

            menuItemDisconnect?.isEnabled = isLocked
            menuItemSamplingFrequency?.isEnabled = isLocked
            menuItemConnect?.isEnabled = !isLocked
            menuItemDisableVisualView?.isEnabled = !isLocked
        }.subscribeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    override fun setupEmgClientDriverConfigViews(clients: List<EmgClientDriver>) {
        renderView?.setupEmgClientDriverConfigViews(clients)
    }

    override fun setupEmgClientDriverView(clients: List<EmgClientDriver>, defaultClient: EmgClientDriver) {
        renderView?.setupEmgClientDriverView(clients, defaultClient)
    }

    override fun setupFilterViews(filter: List<Filter>) {
        renderView?.setupFilterViews(filter)
    }

    override fun setupToolsView(tools: List<Tool>, presenter: EmgPresenter) {
        renderView?.setupToolsView(tools, presenter)
    }

    override fun setupView(viewCallback: EmgViewCallback, config: EmgConfig) {
        this.viewCallback = viewCallback
        this.config = config

        renderView?.setupView(viewCallback, config)
    }

    override fun setupFrequencyAnalysisMethods(methods: List<FrequencyAnalysisMethod>) {
        renderView?.setupFrequencyAnalysisMethods(methods)
    }

    override fun showFrequencyAnalysisView(method: FrequencyAnalysisMethod, data: DoubleArray) {
        renderView?.showFrequencyAnalysisView(method, data)
    }

    override fun updateStatus(status: String) {
        // UiThread is needed, because connection error occurs on different thread
        runOnUiThread {
            renderView?.updateStatus(status)
        }
    }

    override fun showConnectionError(throwable: Throwable) {
        // UiThread is needed, because connection error occurs on different thread
        runOnUiThread {
            renderView?.showConnectionError(throwable)
        }
    }

    override fun onRenderViewReady() {
    }

    // --------------------------------------------------------------------

    private fun showSettings() {
        startActivity(SettingsActivity.newIntent(this),
                ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
    }

    private fun lockOrientation(lock: Boolean) {
        val orientation = if (lock) ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
        else ActivityInfo.SCREEN_ORIENTATION_SENSOR
        requestedOrientation = orientation
    }

    private fun attachEmgView() {
        lockDeviceControls(false)
        emgPresenter.androidEmgView = this
    }

    private fun showExportDialogFragment() {
        TextEnterDialogFragment.newInstance("Export file as", hint = "Path")
                .setOnTextEnteredListener { text ->
                    viewCallback.exportData(text, CsvEmgDataStorage())
                }
                .show(supportFragmentManager, "dialog-export")
    }

    private fun showSamplingFrequencyDialog() {
        TextEnterDialogFragment
                .newInstance("Set sampling frequency", hint = "Frequency in Hz", numbersOnly = true)
                .setOnTextEnteredListener { text ->
                    viewCallback.setSamplingFrequency(text.toDouble())
                }
                .show(supportFragmentManager, "dialog-fs")
    }

    private fun disconnectFromDevice() {

        if (config.isWriteToLogEnabled) {
            TextEnterDialogFragment.newInstance("Save recording as", hint = "Path")
                    .setOnTextEnteredListener { text ->
                        viewCallback.disconnectFromClient(text)
                    }
                    .show(supportFragmentManager, "dialog-save-as")
        } else {
            viewCallback.disconnectFromClient(null)
        }
    }

    private fun showRenderView(savedInstanceState: Bundle?) {

        val tag = "main-fragment"
        val fragment: MainFragment
        if (savedInstanceState == null) {
            fragment = MainFragment.newInstance()
            showFragment(fragment, tag)
        } else {
            fragment = supportFragmentManager.findFragmentByTag(tag) as MainFragment
        }

        // Store reference to renderView, which will be called in the callbacks of EmgView
        renderView = fragment
    }

    private fun showFragment(fragment: Fragment, tag: String? = null) {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.main_container, fragment, tag)
                .commit()
    }

    private fun showFragmentWithBackstack(fragment: Fragment, tag: String? = null) {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.main_container, fragment, tag)
                .addToBackStack(null)
                .commit()
    }

}
