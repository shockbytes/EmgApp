package at.fhooe.mc.emg.app.ui.fragment

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.widget.PopupMenu
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.app.util.AppUtils
import at.fhooe.mc.emg.app.view.AndroidEmgView
import at.fhooe.mc.emg.app.view.OnRenderViewReadyListener
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
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


/**
 * @author Martin Macheiner
 * Date: 01.12.2017.
 */
class MainFragment : BaseFragment(), AndroidEmgView<View> {


    @BindView(R.id.fragment_main_scroll_view_console)
    protected lateinit var scrollViewConsole: ScrollView

    @BindView(R.id.fragment_main_layout)
    protected lateinit var layout: LinearLayout

    @BindView(R.id.fragment_main_txt_console)
    protected lateinit var txtConsole: TextView

    @BindView(R.id.fragment_main_txt_status)
    protected lateinit var txtStatus: TextView

    @BindView(R.id.fragment_main_btn_clients)
    protected lateinit var btnClients: Button

    @BindView(R.id.fragment_main_btn_filter)
    protected lateinit var btnFilter: Button

    @BindView(R.id.fragment_main_btn_analysis)
    protected lateinit var btnAnalysis: Button

    @BindView(R.id.fragment_main_btn_tools)
    protected lateinit var btnTools: Button

    private var visualView: VisualView<View>? = null

    private lateinit var viewCallback: EmgViewCallback

    private lateinit var config: EmgConfig

    private var renderViewListener: OnRenderViewReadyListener? = null

    private var rawDisposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_main, container, false)
    }

    override fun setupViews() {
        txtConsole.movementMethod = ScrollingMovementMethod()
        txtStatus.text = getString(R.string.status_not_connected)
        setupAnalysisViews()

        // Inform MainActivity, that we are now ready to receive callbacks
        renderViewListener?.onRenderViewReady()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        renderViewListener = context as? OnRenderViewReadyListener
    }

    override fun onDestroyView() {
        layout.removeView(visualView?.view)
        if (rawDisposable?.isDisposed == false) {
            rawDisposable?.dispose()
        }
        super.onDestroyView()
    }

    // --------------------------------------------------------------------

    override fun exposeRawClientDataObservable(observable: Observable<String>) {

        var counter = 0
        rawDisposable = observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .buffer(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe {

                    if (it.size > 0) {

                        // Clear every 10 seconds
                        if (counter > 20) {
                            txtConsole.text = ""
                            counter = 0
                        }
                        txtConsole.append("\n")
                        txtConsole.append(it.joinToString("\n"))
                        scrollViewConsole.fullScroll(ScrollView.FOCUS_DOWN)

                        counter++
                    }
                }
    }

    override fun setVisualView(view: VisualView<View>) {
        this.visualView = view

        // Set the correct layout weight and add margin
        val isPortrait = (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
        val lp = if (isPortrait)
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0) else
            LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT)

        val addIdx = if (isPortrait) 1 else 0

        lp.weight = if (isPortrait) 0.5f else 0.75f
        val m = AppUtils.dp2Pixel(context, 8f)
        lp.setMargins(m, m, m, m)
        visualView?.view?.layoutParams = lp

        layout.addView(visualView?.view, addIdx)
    }

    override fun reset() {
        visualView?.reset()
        txtConsole.text = ""
    }

    override fun lockDeviceControls(isLocked: Boolean) {

        // Logic is in reverse for connection and channels
        btnFilter.isEnabled = !isLocked
        btnClients.isEnabled = !isLocked
    }

    override fun setupEmgClientDriverConfigViews(clients: List<EmgClientDriver>) {
        // Will be handled in parent AndroidEmgView
    }

    override fun setupEmgClientDriverView(clients: List<EmgClientDriver>, defaultClient: EmgClientDriver) {

        val menu = PopupMenu(context, btnClients)
        clients.forEach { menu.menu.add(it.shortName) }

        menu.setOnMenuItemClickListener {

            val client = AppUtils.getClientDriverByName(clients, it.title as String)
            viewCallback.setSelectedClient(client!!) // Should never cause a NPE
            btnClients.setCompoundDrawablesWithIntrinsicBounds(0,
                    AppUtils.iconForClient(client.category), 0, 0)
            true
        }
        btnClients.setOnClickListener { menu.show() }
    }

    override fun setupFilterViews(filter: List<Filter>) {

        val menu = PopupMenu(context, btnClients)
        filter.forEachIndexed { idx, f ->
            menu.menu.add(f.name)
                    .setCheckable(true).isChecked = (idx == 0)
        }
        menu.setOnMenuItemClickListener {
            it.isChecked = !it.isChecked
            val f = AppUtils.getFilterByName(filter, it.title as String)
            f?.isEnabled = it.isChecked
            true
        }
        btnFilter.setOnClickListener { menu.show() }
    }

    override fun setupToolsView(tools: List<Tool>, controller: EmgController) {

        val menu = PopupMenu(context, btnTools)
        tools.forEach { menu.menu.add(it.name) }

        menu.setOnMenuItemClickListener {
            val t: Tool? = AppUtils.getToolByName(tools, it.title as String)
            t?.start(controller)
            true
        }
        btnTools.setOnClickListener { menu.show() }
    }

    override fun setupView(viewCallback: EmgViewCallback, config: EmgConfig) {
        this.viewCallback = viewCallback
        this.config = config
    }

    override fun showFrequencyAnalysisView(method: FrequencyAnalysisMethod) {
        // Will be handled in parent AndroidEmgView
    }

    override fun updateStatus(status: String) {
        txtStatus.text = status
    }

    // --------------------------------------------------------------------

    private fun setupAnalysisViews() {

        val menu = PopupMenu(context, btnAnalysis)
        menu.menuInflater.inflate(R.menu.menu_analysis, menu.menu)
        menu.setOnMenuItemClickListener {

            when (it.itemId) {

                R.id.menu_analysis_fft -> {
                    viewCallback.requestFrequencyAnalysisView(FrequencyAnalysisMethod.Method.FFT)
                }
                R.id.menu_analysis_power_spectrum -> {
                    viewCallback.requestFrequencyAnalysisView(FrequencyAnalysisMethod.Method.SPECTRUM)
                }
            }
            true
        }
        btnAnalysis.setOnClickListener { menu.show() }
    }

    companion object {

        fun newInstance(): MainFragment {
            val fragment = MainFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }

    }

}