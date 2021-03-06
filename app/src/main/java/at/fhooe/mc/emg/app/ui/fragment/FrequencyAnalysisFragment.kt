package at.fhooe.mc.emg.app.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.TextView
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.core.analysis.FrequencyAnalysisView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotterknife.bindView

/**
 * Author:  Martin Macheiner
 * Date:    01.12.2017.
 */
class FrequencyAnalysisFragment : BaseFragment(), FrequencyAnalysisView {

    override val layoutId = R.layout.fragment_frequency_analysis

    private val chart: LineChart by bindView(R.id.fragment_frequency_analysis_chart)
    private val txtTitle: TextView by bindView(R.id.fragment_frequency_analysis_title)

    override fun showEvaluation(method: String, xData: DoubleArray, yData: DoubleArray) {

        if (xData.size != yData.size || xData.isEmpty()) {
            fragmentManager.popBackStack()
            return
        }

        val list = ArrayList<BarEntry>()
        (0 until xData.size).mapIndexedTo(list) { idx, _ ->
            BarEntry(xData[idx].toFloat(), yData[idx].toFloat())
        }
        val set = createDataSet(method, list)

        // Computation is done on another thread
        activity.runOnUiThread {
            txtTitle.text = method
            chart.data.addDataSet(set)
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
            chart.invalidate()
        }
    }

    override fun setupViews() {
        setupChart()
    }

    override fun showError(error: Throwable) {
        val msg =  "${error.javaClass.simpleName}: ${error.localizedMessage}"
        showToast(msg, true)
        fragmentManager.popBackStack()
    }


    private fun setupChart() {
        chart.description.isEnabled = false
        chart.setTouchEnabled(true)
        chart.setPinchZoom(true)
        chart.setScaleEnabled(true)
        chart.setDrawGridBackground(false)

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

    private fun createDataSet(title: String, yValues: List<Entry>): LineDataSet {
        val set = LineDataSet(yValues, title)
        set.axisDependency = YAxis.AxisDependency.LEFT
        set.setDrawValues(false)
        set.setCircleColor(ContextCompat.getColor(context, R.color.okay))
        set.color = ContextCompat.getColor(context, R.color.okay)
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.valueTextSize = 10f
        return set
    }


    companion object {

        fun newInstance(): FrequencyAnalysisFragment {
            val fragment = FrequencyAnalysisFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }

    }

}