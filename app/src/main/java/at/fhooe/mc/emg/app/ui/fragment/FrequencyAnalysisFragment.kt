package at.fhooe.mc.emg.app.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.app.util.AppUtils
import at.fhooe.mc.emg.core.analysis.FrequencyAnalysisMethod
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
 * @author Martin Macheiner
 * Date: 01.12.2017.
 */
class FrequencyAnalysisFragment : BaseFragment(), FrequencyAnalysisView {

    override val layoutId = R.layout.fragment_frequency_analysis

    private val chart: LineChart by bindView(R.id.fragment_frequency_analysis_chart)
    private val txtTitle: TextView by bindView(R.id.fragment_frequency_analysis_title)

    override fun showEvaluation(method: FrequencyAnalysisMethod.Method, xData: DoubleArray, yData: DoubleArray) {

        if (xData.size != yData.size || xData.isEmpty()) {
            fragmentManager.popBackStack()
            return
        }

        val title = getString(AppUtils.getFrequencyAnalysisTitleByMethod(method))
        val list = ArrayList<BarEntry>()
        (0 until xData.size).mapIndexedTo(list) { idx, _ ->
            BarEntry(xData[idx].toFloat(), yData[idx].toFloat())
        }
        val set = createDataSet(title, list)

        // Computation is done on another thread
        activity.runOnUiThread {
            txtTitle.text = title
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

    private fun createDataSet(title: String, yValues: List<Entry>): LineDataSet {
        val set = LineDataSet(yValues, title)
        set.axisDependency = YAxis.AxisDependency.LEFT
        set.setDrawValues(false)
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