package at.fhooe.mc.emg.app.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.PopupMenu
import android.text.method.ScrollingMovementMethod
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.TextView
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.app.ui.fragment.config.AndroidConfigView
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
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotterknife.bindView
import java.util.concurrent.TimeUnit


/**
 * @author Martin Macheiner
 * Date: 01.12.2017.
 */

class MainFragment : BaseFragment(), AndroidEmgView<View> {

    private val scrollViewConsole: ScrollView by bindView(R.id.fragment_main_scroll_view_console)

    private val visualContainer: FrameLayout by bindView(R.id.fragment_main_visualview_container)
    private val txtConsole: TextView by bindView(R.id.fragment_main_txt_console)
    private val txtStatus: TextView by bindView(R.id.fragment_main_txt_status)
    private val btnClients: Button by bindView(R.id.fragment_main_btn_clients)
    private val btnFilter: Button by bindView(R.id.fragment_main_btn_filter)
    private val btnAnalysis: Button by bindView(R.id.fragment_main_btn_analysis)
    private val btnTools: Button by bindView(R.id.fragment_main_btn_tools)

    private var visualView: VisualView<View>? = null

    private lateinit var viewCallback: EmgViewCallback

    private lateinit var config: EmgConfig

    private var renderViewListener: OnRenderViewReadyListener? = null

    private var rawDisposable: Disposable? = null

    override val layoutId = R.layout.fragment_main

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
        visualContainer.removeAllViews()
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
                .buffer(AppUtils.bufferSpan, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe {

                    if (it.size > 0) {
                        // Clear every 2.5 seconds
                        if (counter > 10) {
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
        visualContainer.addView(visualView?.view)
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

        val menu = PopupMenu(context, btnClients)
        clients.forEach {
            if (it.hasConfigView) {
                menu.menu.add(it.configView?.name)
            }
        }

        menu.setOnMenuItemClickListener {
            val client = AppUtils.getClientDriverByConfigViewName(clients, it.title as String)
            // Workaround for Android, as DialogFragment must be explicitly called!
            // And the ConfigView must be initialized first
            val configView = client?.configView as? AndroidConfigView
            configView?.setOnViewReadyListener { client.configView?.show(client) }
                    ?.show(fragmentManager, "cv-${it.title}")
            true
        }
        btnClients.setOnLongClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            menu.show()
            true
        }
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
        btnClients.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            menu.show()
        }
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

        // Select raw filter by default
        filter[0].isEnabled = true

        btnFilter.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            menu.show()
        }
    }

    override fun setupToolsView(tools: List<Tool>, controller: EmgController) {

        val menu = PopupMenu(context, btnTools)
        tools.forEach { menu.menu.add(it.name) }

        menu.setOnMenuItemClickListener {
            val t: Tool? = AppUtils.getToolByName(tools, it.title as String)

            // Workaround for Android! Fragment must be explicitly called!
            // And the ConfigView must be initialized first
            val toolView = t?.view as? AndroidToolViewFragment
            toolView?.setOnViewReadyListener { t.start(controller, false) }
            showFragmentWithBackStack(toolView, "tv-${t?.name}")
            true
        }
        btnTools.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            menu.show()
        }
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

    override fun showConnectionError(throwable: Throwable) {
        val msg = "${throwable.javaClass.simpleName}: ${throwable.localizedMessage}"
        showToast(msg, true)
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
        btnAnalysis.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            menu.show()
        }
    }

    private fun showFragmentWithBackStack(fragment: Fragment?, tag: String) {
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.main_container, fragment, tag)
                .addToBackStack(null)
                .commit()
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