package at.fhooe.mc.emg.app.ui.fragment

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.app.view.AndroidEmgView
import at.fhooe.mc.emg.clientdriver.EmgClientDriver
import at.fhooe.mc.emg.core.EmgController
import at.fhooe.mc.emg.core.analysis.FrequencyAnalysisMethod
import at.fhooe.mc.emg.core.filter.Filter
import at.fhooe.mc.emg.core.tools.Tool
import at.fhooe.mc.emg.core.util.config.EmgConfig
import at.fhooe.mc.emg.core.view.EmgViewCallback
import at.fhooe.mc.emg.core.view.VisualView
import at.shockbytes.remote.fragment.BaseFragment
import butterknife.BindView

/**
 * @author Martin Macheiner
 * Date: 01.12.2017.
 */
class MainFragment : BaseFragment(), AndroidEmgView<View> {

    @BindView(R.id.fragment_main_txt_console)
    protected lateinit var txtConsole: TextView

    @BindView(R.id.fragment_main_txt_status)
    protected lateinit var txtStatus: TextView

    private var visualView: VisualView<View>? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_main, container, false)
    }

    override fun setupViews() {
        // TODO
        txtConsole.movementMethod = ScrollingMovementMethod()
        txtStatus.text = getString(R.string.status_not_connected)
    }

    // --------------------------------------------------------------------

    override fun onRawClientDataAvailable(raw: String) {
        txtConsole.append("$raw\n")

    }

    override fun setVisualView(view: VisualView<View>) {
        this.visualView = view

        // TODO Replace views
    }

    override fun reset() {
        visualView?.reset()
        txtConsole.text = ""
    }

    override fun setDeviceControlsEnabled(isEnabled: Boolean) {
        // TODO
    }

    override fun setupEmgClientDriverConfigViews(clients: List<EmgClientDriver>) {
        // Will be handled in parent AndroidEmgView
    }

    override fun setupEmgClientDriverView(clients: List<EmgClientDriver>, defaultClient: EmgClientDriver) {
        // TODO
    }

    override fun setupFilterViews(filter: List<Filter>) {
        // TODO
    }

    override fun setupToolsView(tools: List<Tool>, controller: EmgController) {
        // TODO
    }

    override fun setupView(viewCallback: EmgViewCallback, config: EmgConfig) {
        // TODO
    }

    override fun showFrequencyAnalysisView(method: FrequencyAnalysisMethod) {
        // Will be handled in parent AndroidEmgView
    }

    override fun updateStatus(status: String) {
        // TODO
    }

    // --------------------------------------------------------------------

    companion object {

        fun newInstance(): MainFragment {
            val fragment = MainFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }

    }

}