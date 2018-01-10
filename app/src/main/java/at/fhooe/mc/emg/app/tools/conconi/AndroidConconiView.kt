package at.fhooe.mc.emg.app.tools.conconi

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.Button
import android.widget.TextView
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.app.adapter.ConconiRoundDataAdapter
import at.fhooe.mc.emg.app.ui.fragment.AndroidToolViewFragment
import at.fhooe.mc.emg.app.ui.fragment.dialog.PickFilesDialogFragment
import at.fhooe.mc.emg.app.ui.fragment.dialog.TextEnterDialogFragment
import at.fhooe.mc.emg.app.util.AppUtils
import at.fhooe.mc.emg.core.tools.conconi.ConconiRoundData
import at.fhooe.mc.emg.core.tools.conconi.ConconiView
import at.fhooe.mc.emg.core.tools.conconi.ConconiViewCallback
import at.shockbytes.util.view.EqualSpaceItemDecoration
import butterknife.OnClick
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import kotterknife.bindView


/**
 * @author Martin Macheiner
 * Date: 06.12.2017.
 */

class AndroidConconiView : AndroidToolViewFragment(), ConconiView {

    override val layoutId = R.layout.fragment_conconi_view

    private var viewCallback: ConconiViewCallback? = null

    private val btnStartStop: Button by bindView(R.id.fragment_conconi_btn_start)
    private val btnLoad: Button by bindView(R.id.fragment_conconi_btn_load)
    private val chart: LineChart by bindView(R.id.fragment_conconi_linechart)
    private val rvRoundData: RecyclerView by bindView(R.id.fragment_conconi_recyclerview)
    private val txtCounter: TextView by bindView(R.id.fragment_conconi_txt_counter)

    private var isRunning = false

    private var errorHandler: Consumer<Throwable> = Consumer { throwable: Throwable ->
        Single.fromCallable {
            val text = "${throwable::class.java}: ${throwable.localizedMessage}"
            showToast(text, true)
        }.subscribeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    private lateinit var roundAdapter: ConconiRoundDataAdapter


    override fun setupViews() {
        updateButtonStates(true)
        setupRecyclerView()
        setupChart()
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
        Completable.fromAction {
            txtCounter.text = seconds.toString()
        }.subscribeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    override fun onRoundDataAvailable(data: ConconiRoundData, round: Int) {
        Completable.fromAction {
            roundAdapter.addEntityAtLast(data)
            updateChart(data, round)
        }.subscribeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    override fun onTick(seconds: Int, goal: Int) {
        Completable.fromAction {
            val out = "$seconds / $goal seconds"
            txtCounter.text = out
        }.subscribeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    override fun onPlayCountdownSound() {
        AppUtils.playSound(context, R.raw.conconi_countdown)
    }

    @OnClick(R.id.fragment_conconi_btn_start)
    protected fun onClickStart(v: View) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)

        if (!isRunning) {
            isRunning = true
            viewCallback?.onStartClicked()
            btnStartStop.text = getString(R.string.stop)
            btnStartStop.setCompoundDrawablesWithIntrinsicBounds(0,
                    R.drawable.ic_menu_disconnect, 0, 0)
        } else {
            isRunning = false
            viewCallback?.onStopClicked()
            btnStartStop.text = getString(R.string.start)
            btnStartStop.setCompoundDrawablesWithIntrinsicBounds(0,
                    R.drawable.ic_menu_connect, 0, 0)
            txtCounter.text = getString(R.string.tool_conconi_test_finished)
        }
        updateButtonStates(!isRunning)
    }

    @OnClick(R.id.fragment_conconi_btn_save)
    protected fun onClickSave(v: View) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)

        TextEnterDialogFragment.newInstance("Enter filename", hint = "Conconi filename")
                .setOnTextEnteredListener {
                    viewCallback?.onSaveClicked("${AppUtils.conconiDirectory}/$it.ctf", errorHandler)
                }.show(fragmentManager, "conconi-save-dialogfragment")
    }

    @OnClick(R.id.fragment_conconi_btn_load)
    protected fun onClickLoad(v: View) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)

        viewCallback?.requestStoredConconiFiles(AppUtils.conconiDirectory, true)
                ?.subscribe { list: List<String>? ->
                    list?.let {
                        PickFilesDialogFragment.newInstance(list)
                                .setOnFilePickedListener {
                                    viewCallback?.onLoadClicked("${AppUtils.conconiDirectory}/$it",
                                            errorHandler)
                                }.show(fragmentManager, "conconi-pick-files-dialogfragment")
                    }
                }
    }

    private fun updateChart(data: ConconiRoundData, round: Int) {
        addChartEntry(Entry(round.toFloat(), data.avg.toFloat()))
    }

    private fun updateButtonStates(isEnabled: Boolean) {
        btnLoad.isEnabled = isEnabled
        val textId = if (isEnabled) R.string.start else R.string.stop
        btnStartStop.text = getString(textId)
    }

    private fun setupRecyclerView() {
        roundAdapter = ConconiRoundDataAdapter(context, mutableListOf())
        rvRoundData.addItemDecoration(EqualSpaceItemDecoration(AppUtils.dp2Pixel(context, 2f)))
        rvRoundData.layoutManager = LinearLayoutManager(context)
        rvRoundData.adapter = roundAdapter
    }

    private fun setupChart() {

        chart.description.isEnabled = false
        chart.setTouchEnabled(true)
        chart.setPinchZoom(false)
        chart.setScaleEnabled(true)
        chart.setDrawGridBackground(false)
        chart.legend.textColor = ContextCompat.getColor(context, R.color.legendTextColor)
        chart.setVisibleXRangeMaximum(6f)

        // Axes
        val xl = chart.xAxis
        xl.textColor = Color.DKGRAY
        xl.setDrawGridLines(false)
        xl.setAvoidFirstLastClipping(true)
        xl.isEnabled = true
        val leftAxis = chart.axisLeft
        leftAxis.textColor = Color.DKGRAY
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawGridLines(false)
        val rightAxis = chart.axisRight
        rightAxis.isEnabled = false

        leftAxis.setDrawLimitLinesBehindData(true)

        // Data
        val data = LineData()
        data.setValueTextColor(Color.DKGRAY)
        chart.data = data

        // Legend
        val l = chart.legend
        l.form = Legend.LegendForm.CIRCLE
        l.textColor = Color.DKGRAY
    }

    private fun addChartEntry(e: Entry) {

        val data = chart.data
        var set: ILineDataSet? = data.getDataSetByIndex(0)

        if (set == null) {
            set = LineDataSet(null, "Average")
            set.setCircleColor(ContextCompat.getColor(context, R.color.blue))
            set.color = ContextCompat.getColor(context, R.color.blue)
            data.addDataSet(set)
        }

        data.addEntry(e, 0)
        data.notifyDataChanged()
        chart.notifyDataSetChanged()
        chart.setVisibleXRangeMaximum(6f)
        chart.moveViewTo(data.entryCount - 7f, 50f, AxisDependency.LEFT)
    }

}