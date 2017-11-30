package at.fhooe.mc.emg.app.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.app.core.AndroidEmgController
import at.fhooe.mc.emg.app.core.EmgApp
import at.fhooe.mc.emg.app.view.AndroidEmgView
import at.fhooe.mc.emg.clientdriver.EmgClientDriver
import at.fhooe.mc.emg.core.EmgController
import at.fhooe.mc.emg.core.analysis.FrequencyAnalysisMethod
import at.fhooe.mc.emg.core.filter.Filter
import at.fhooe.mc.emg.core.tools.Tool
import at.fhooe.mc.emg.core.util.Configuration
import at.fhooe.mc.emg.core.view.EmgViewCallback
import at.fhooe.mc.emg.core.view.VisualView
import javax.inject.Inject

class MainActivity : AppCompatActivity(), AndroidEmgView<View> {

    @Inject
    protected lateinit var emgController: AndroidEmgController

    private lateinit var viewCallback: EmgViewCallback

    private lateinit var config: Configuration

    private lateinit var visualView: VisualView<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as EmgApp).appComponent.inject(this)
        emgController.androidEmgView = this
    }

    // --------------------------------------------------------------------

    override fun setVisualView(view: VisualView<View>) {
        visualView = view
        // TODO Set the view in the layout
    }

    override fun onRawClientDataAvailable(raw: String) {
        // TODO
    }

    override fun reset() {
        // TODO
    }

    override fun setDeviceControlsEnabled(isEnabled: Boolean) {
        // TODO
    }

    override fun setupEmgClientDriverConfigViews(clients: List<EmgClientDriver>) {
        // TODO
    }

    override fun setupEmgClientDriverView(clients: List<EmgClientDriver>, defaultClient: EmgClientDriver) {
        // TODO Set the Config view for each driver here?
        // TODO
        clients.forEach { Toast.makeText(applicationContext, it.shortName, Toast.LENGTH_SHORT).show() }
    }

    override fun setupFilterViews(filter: List<Filter>) {
        // TODO
    }

    override fun setupToolsView(tools: List<Tool>, controller: EmgController) {
        // TODO
    }

    override fun setupView(viewCallback: EmgViewCallback, config: Configuration) {
        this.viewCallback = viewCallback
        this.config = config

        // TODO Read from configuration
    }

    override fun showFrequencyAnalysisView(method: FrequencyAnalysisMethod) {
        // TODO Pass a view to the method
        method.evaluate(null)
    }

    override fun updateStatus(status: String) {
        // TODO
        actionBar.subtitle = status
    }


}
