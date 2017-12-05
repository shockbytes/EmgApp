package at.fhooe.mc.emg.app.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.app.util.AppUtils
import at.fhooe.mc.emg.clientdriver.model.EmgData
import at.fhooe.mc.emg.core.filter.Filter
import at.fhooe.mc.emg.core.view.VisualView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers


/**
 * @author Martin Macheiner
 * Date: 28.11.2017.
 */
class AndroidVisualView(private val context: Context,
                        private val windowSize: Int) : VisualView<View> {

    override val dataForFrequencyAnalysis: DoubleArray
        get() {
            // TODO
            return DoubleArray(0)
        }

    override val bufferSpan: Long = AppUtils.bufferSpan

    override val scheduler: Scheduler? = AndroidSchedulers.mainThread()

    override val view: View
        get() = chart

    private lateinit var chart: LineChart

    init {
        initialize()
    }

    @SuppressLint("InflateParams")
    override fun initialize() {
        chart = LayoutInflater.from(context)
                .inflate(R.layout.visual_view, null, false) as LineChart
        setupChart()
    }

    private fun setupChart() {
        chart.description.isEnabled = false
        chart.setTouchEnabled(false)
        chart.setPinchZoom(false)
        chart.setScaleEnabled(true)
        chart.setDrawGridBackground(false)

        // Axes
        val xl = chart.xAxis
        xl.textColor = Color.WHITE
        xl.setDrawGridLines(false)
        xl.setAvoidFirstLastClipping(true)
        xl.isEnabled = true
        val leftAxis = chart.axisLeft
        leftAxis.textColor = Color.WHITE
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawGridLines(false)
        val rightAxis = chart.axisRight
        rightAxis.isEnabled = false

        leftAxis.setDrawLimitLinesBehindData(true)

        // Data
        val data = LineData()
        data.setValueTextColor(Color.WHITE)
        chart.data = data

        // Legend
        val l = chart.legend
        l.form = Legend.LegendForm.CIRCLE
        l.textColor = Color.WHITE
    }

    override fun reset() {
        chart.data.clearValues()
        chart.invalidate()
    }

    override fun setYMaximum(maximum: Double) {
        chart.axisLeft.axisMaximum = maximum.toFloat()
    }

    override fun update(data: EmgData, filters: List<Filter>) {

        for (i in 0 until data.channelCount) {
            filters
                    .filter { it.isEnabled }
                    .forEach { filter ->
                        val supposedName = (i + 1).toString() + "." + filter.shortName
                        val set = getSetByName(supposedName)
                        val isNewlyCreated = set.entryCount == 0
                        addToDataSet(set, data, i, filter)
                        if (isNewlyCreated) {
                            chart.data.addDataSet(set)
                        }
                    }
        }
        invalidateChart()
    }

    // ----------------------------------------------------------------------------------

    private fun channelData2Entries(data: EmgData, channel: Int,
                                    filter: Filter): List<Entry> {
        return data.plotData(channel)
                .map { Entry(it.x.toFloat(), filter.step(it.y).toFloat()) }
    }

    private fun invalidateChart() {

        // let the chart know it's data has changed
        chart.data.notifyDataChanged()
        chart.notifyDataSetChanged()

        // Let's scroll the with window
        chart.setVisibleXRangeMaximum(windowSize.toFloat())
        // move to the latest entry
        val end = chart.data.getDataSetByIndex(0).xMax
        chart.moveViewToX(end)
    }

    private fun getSetByName(name: String): ILineDataSet {
        chart.data.dataSets.forEach {
            if (it.label == name) {
                return it
            }
        }
        return createDataSet(name)
    }

    private fun addToDataSet(set: ILineDataSet?, data: EmgData, channel: Int, filter: Filter) {
        set?.clear()
        channelData2Entries(data, channel, filter).forEach { set?.addEntry(it) }
    }


    private fun createDataSet(title: String): ILineDataSet {
        val set = LineDataSet(null, title)
        set.axisDependency = YAxis.AxisDependency.LEFT
        set.lineWidth = 1f
        set.setDrawValues(false)
        set.circleRadius = 2f
        set.circleColors = set.colors
        set.valueTextSize = 10f
        return set
    }


}