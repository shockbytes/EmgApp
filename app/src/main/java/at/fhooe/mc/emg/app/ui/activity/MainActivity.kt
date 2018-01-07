package at.fhooe.mc.emg.app.ui.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.app.core.AndroidEmgController
import at.fhooe.mc.emg.app.core.EmgApp
import at.fhooe.mc.emg.app.ui.fragment.FrequencyAnalysisFragment
import at.fhooe.mc.emg.app.ui.fragment.MainFragment
import at.fhooe.mc.emg.app.ui.fragment.dialog.TextEnterDialogFragment
import at.fhooe.mc.emg.app.view.AndroidEmgView
import at.fhooe.mc.emg.app.view.OnRenderViewReadyListener
import at.fhooe.mc.emg.clientdriver.EmgClientDriver
import at.fhooe.mc.emg.core.EmgController
import at.fhooe.mc.emg.core.analysis.FrequencyAnalysisMethod
import at.fhooe.mc.emg.core.filter.Filter
import at.fhooe.mc.emg.core.storage.CsvDataStorage
import at.fhooe.mc.emg.core.tools.Tool
import at.fhooe.mc.emg.core.util.config.EmgConfig
import at.fhooe.mc.emg.core.view.EmgViewCallback
import at.fhooe.mc.emg.core.view.VisualView
import io.reactivex.Observable
import javax.inject.Inject

class MainActivity : AppCompatActivity(), AndroidEmgView<View>, OnRenderViewReadyListener {

    @Inject
    protected lateinit var emgController: AndroidEmgController

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
        (application as EmgApp).appComponent.inject(this)
        showRenderView(savedInstanceState)
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
        renderView?.reset()
    }

    override fun lockDeviceControls(isLocked: Boolean) {
        renderView?.lockDeviceControls(isLocked)

        menuItemDisconnect?.isEnabled = isLocked
        menuItemSamplingFrequency?.isEnabled = isLocked
        menuItemConnect?.isEnabled = !isLocked
        menuItemDisableVisualView?.isEnabled = !isLocked
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

    override fun setupToolsView(tools: List<Tool>, controller: EmgController) {
        renderView?.setupToolsView(tools, controller)
    }

    override fun setupView(viewCallback: EmgViewCallback, config: EmgConfig) {
        this.viewCallback = viewCallback
        this.config = config

        renderView?.setupView(viewCallback, config)
    }

    override fun showFrequencyAnalysisView(method: FrequencyAnalysisMethod) {
        val fragment = FrequencyAnalysisFragment.newInstance()
        // Only evaluate method if it is already built!
        fragment.setOnViewReadyListener {
            method.evaluate(fragment)
        }
        showFragmentWithBackstack(fragment)
    }

    override fun updateStatus(status: String) {
        renderView?.updateStatus(status)
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
        emgController.androidEmgView = this
    }

    private fun showExportDialogFragment() {
        TextEnterDialogFragment.newInstance("Export file as", hint = "Path")
                .setOnTextEnteredListener { text ->
                    viewCallback.exportData(text, CsvDataStorage())
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
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.main_container, fragment, tag)
                .addToBackStack(null)
                .commit()
    }

}
