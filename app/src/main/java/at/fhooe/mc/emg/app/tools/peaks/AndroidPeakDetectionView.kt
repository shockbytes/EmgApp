package at.fhooe.mc.emg.app.tools.peaks

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.app.ui.fragment.AndroidToolViewFragment
import at.fhooe.mc.emg.core.tool.peaks.Peak
import at.fhooe.mc.emg.core.tool.peaks.PeakDetectionToolView
import at.fhooe.mc.emg.core.tool.peaks.PeakDetectionToolViewCallback
import at.fhooe.mc.emg.core.tool.peaks.PeakDetector
import butterknife.OnClick
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import kotterknife.bindView

/**
 * @author Martin Macheiner
 * Date: 08.01.2017.
 */
class AndroidPeakDetectionView : AndroidToolViewFragment(), PeakDetectionToolView {

    private var viewCallback: PeakDetectionToolViewCallback? = null

    private val txtHeader: TextView by bindView(R.id.fragment_peak_detection_txt_header)
    private val txtListOutput: TextView by bindView(R.id.fragment_peaks_txt_output)
    private val chart: LineChart by bindView(R.id.fragment_peaks_linechart)
    private val progressBar: ProgressBar by bindView(R.id.fragment_peaks_progressbar)

    override val layoutId = R.layout.fragment_peak_detection_view

    override fun setupViews() {
        setupChart()
    }

    override fun showView() {
        showToolView("tv-peak-detection")
    }

    override fun setup(toolViewCallback: PeakDetectionToolViewCallback, showViewImmediate: Boolean) {
        this.viewCallback = toolViewCallback

        if (showViewImmediate) {
            showView()
        }
    }

    override fun showError(cause: String, title: String) {
        Single.fromCallable {
            showToast("$title\n$cause", true)
            progressBar.animate().alpha(0f).start()
        }.subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    override fun showPeaksDetail(peaks: List<Peak>) {
        Single.fromCallable {
            txtHeader.text = getString(R.string.tool_peaks_title_detected, peaks.size)
            txtListOutput.text = peaks.joinToString("\n") { it.toPrettyString() }
        }.subscribeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    override fun showPlotData(xValues: DoubleArray, yValues: DoubleArray,
                              xValuesPeaks: DoubleArray, yValuesPeaks: DoubleArray) {
        Single.fromCallable {
            setChartData(xValues, yValues, xValuesPeaks, yValuesPeaks)
        }.subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe { _ -> progressBar.animate().alpha(0f).start() }
    }

    @OnClick(R.id.fragment_peaks_btn_compute)
    protected fun onClickCompute(v: View) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        progressBar.animate().alpha(1f).start()
        viewCallback?.updateParameter(PeakDetector.defaultWidth, PeakDetector.defaultThreshold,
                PeakDetector.defaultDecayRate, PeakDetector.defaultIsRelative)

        viewCallback?.computeManually()
    }

    @OnClick(R.id.fragment_peaks_btn_settings)
    protected fun onClickSettings(v: View) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        showToast("Coming soon...")
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

    private fun setChartData(xValues: DoubleArray, yValues: DoubleArray,
                             xValuesPeaks: DoubleArray, yValuesPeaks: DoubleArray) {

        val data = chart.data
        data.isHighlightEnabled = false
        data.clearValues()

        val setData = LineDataSet(
                xValues.mapIndexed { idx, it ->  Entry(it.toFloat(), yValues[idx].toFloat())},
                "Data")
        setData.setCircleColor(ContextCompat.getColor(context, R.color.green))
        setData.color = ContextCompat.getColor(context, R.color.green)
        setData.setDrawCircles(false)
        data.addDataSet(setData)

        val setPeaks = LineDataSet(
                xValuesPeaks.mapIndexed {idx, it -> Entry(it.toFloat(), yValuesPeaks[idx].toFloat())},
                "Peaks")
        setPeaks.lineWidth = 0.2f
        setPeaks.circleRadius = 3f
        setPeaks.setCircleColorHole(ContextCompat.getColor(context, R.color.orange))
        setPeaks.setCircleColor(ContextCompat.getColor(context, R.color.orange))
        setPeaks.color = ContextCompat.getColor(context, R.color.orange)
        data.addDataSet(setPeaks)

        invalidateChart()
    }

    private fun invalidateChart() {

        // let the chart know it's data has changed
        chart.data.notifyDataChanged()
        chart.notifyDataSetChanged()
        // move to the latest entry
        val end = chart.data.getDataSetByIndex(0).xMax
        chart.moveViewToX(end)
    }

}