package at.fhooe.mc.emg.app.ui.activity

import android.os.Bundle
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
import at.fhooe.mc.emg.app.view.AndroidEmgView
import at.fhooe.mc.emg.clientdriver.EmgClientDriver
import at.fhooe.mc.emg.core.EmgController
import at.fhooe.mc.emg.core.analysis.FrequencyAnalysisMethod
import at.fhooe.mc.emg.core.filter.Filter
import at.fhooe.mc.emg.core.tools.Tool
import at.fhooe.mc.emg.core.util.config.EmgConfig
import at.fhooe.mc.emg.core.view.EmgViewCallback
import at.fhooe.mc.emg.core.view.VisualView
import javax.inject.Inject

class MainActivity : AppCompatActivity(), AndroidEmgView<View> {

    @Inject
    protected lateinit var emgController: AndroidEmgController

    private lateinit var viewCallback: EmgViewCallback

    private lateinit var config: EmgConfig

    private lateinit var visualView: VisualView<View>

    private var renderView: AndroidEmgView<View>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as EmgApp).appComponent.inject(this)

        showRenderView()

        emgController.androidEmgView = this
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        // TODO Use viewCallback here
        when (item?.itemId) {

        }

        return super.onOptionsItemSelected(item)
    }

    // --------------------------------------------------------------------

    override fun setVisualView(view: VisualView<View>) {
        visualView = view
        renderView?.setVisualView(view)
    }

    override fun onRawClientDataAvailable(raw: String) {
        renderView?.onRawClientDataAvailable(raw)
    }

    override fun reset() {
        // TODO Dispatch partially
        renderView?.reset()
    }

    override fun setDeviceControlsEnabled(isEnabled: Boolean) {
        // TODO Dispatch partially
        renderView?.setDeviceControlsEnabled(isEnabled)
    }

    override fun setupEmgClientDriverConfigViews(clients: List<EmgClientDriver>) {
        // TODO Do that here
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

        // TODO Read from configuration
        renderView?.setupView(viewCallback, config)
    }

    override fun showFrequencyAnalysisView(method: FrequencyAnalysisMethod) {
        val fragment = FrequencyAnalysisFragment.newInstance()
        showFragmentWithBackstack(fragment)
        method.evaluate(fragment)
    }

    override fun updateStatus(status: String) {
        renderView?.updateStatus(status)
    }

    // --------------------------------------------------------------------

    private fun showRenderView() {

        val fragment = MainFragment.newInstance()
        // Store reference to renderView, which will be called in the callbacks of EmgView
        renderView = fragment

        showFragment(fragment)
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.main_container, fragment)
                .commit()
    }

    private fun showFragmentWithBackstack(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit()
    }

}
