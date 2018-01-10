package at.fhooe.mc.emg.app.tools.conconi

import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.TextView
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.app.adapter.ConconiRoundDataAdapter
import at.fhooe.mc.emg.app.ui.fragment.AndroidToolViewFragment
import at.fhooe.mc.emg.core.tools.conconi.ConconiRoundData
import at.fhooe.mc.emg.core.tools.conconi.ConconiView
import at.fhooe.mc.emg.core.tools.conconi.ConconiViewCallback
import butterknife.OnClick
import com.github.mikephil.charting.charts.LineChart
import kotterknife.bindView

/**
 * @author Martin Macheiner
 * Date: 06.12.2017.
 */

class AndroidConconiView : AndroidToolViewFragment(), ConconiView {

    override val layoutId = R.layout.fragment_conconi_view

    private var viewCallback: ConconiViewCallback? = null

    private val btnStartStop: Button by bindView(R.id.fragment_conconi_btn_start)
    private val btnSave: Button by bindView(R.id.fragment_conconi_btn_save)
    private val btnLoad: Button by bindView(R.id.fragment_conconi_btn_load)
    private val lineChart: LineChart by bindView(R.id.fragment_conconi_linechart)
    private val rvRoundData: RecyclerView by bindView(R.id.fragment_conconi_recyclerview)
    private val txtCounter: TextView by bindView(R.id.fragment_conconi_txt_counter)

    private lateinit var roundAdapter: ConconiRoundDataAdapter

    private var isRunning = false

    override fun setupViews() {
        roundAdapter = ConconiRoundDataAdapter(context, mutableListOf())
        updateButtonStates(true)
    }

    override fun setup(viewCallback: ConconiViewCallback, showViewImmediate: Boolean) {
        this.viewCallback = viewCallback

        if (showViewImmediate) {
            showView()
        }
    }

    override fun showView() {
        showToolView("tv-conconi")
    }

    override fun onCountdownTick(seconds: Int) {
        txtCounter.text = seconds.toString()
    }

    override fun onRoundDataAvailable(data: ConconiRoundData, round: Int) {

    }

    override fun onTick(seconds: Int, goal: Int) {
        val out = "$seconds / $goal seconds"
        txtCounter.text = out
    }

    override fun onPlayCountdownSound() {

    }

    @OnClick(R.id.fragment_conconi_btn_start)
    protected fun onClickStart() {

        if (!isRunning) {
            isRunning = true
            viewCallback?.onStartClicked()
        } else {
            isRunning = false
            viewCallback?.onStopClicked()
        }
        updateButtonStates(!isRunning)
    }

    @OnClick(R.id.fragment_conconi_btn_save)
    protected fun onClickSave() {

    }

    @OnClick(R.id.fragment_conconi_btn_load)
    protected fun onClickLoad() {

    }

    private fun updateButtonStates(isEnabled: Boolean) {

        btnLoad.isEnabled = isEnabled
        val textId = if (isEnabled) R.string.start else R.string.stop
        btnStartStop.text = getString(textId)
    }

}