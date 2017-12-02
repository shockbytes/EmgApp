package at.fhooe.mc.emg.app.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.core.analysis.FrequencyAnalysisMethod
import at.fhooe.mc.emg.core.analysis.FrequencyAnalysisView
import at.shockbytes.remote.fragment.BaseFragment
import butterknife.BindView
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint



/**
 * @author Martin Macheiner
 * Date: 01.12.2017.
 */
class FrequencyAnalysisFragment : BaseFragment(), FrequencyAnalysisView {

    @BindView(R.id.fragment_frequency_analysis_graphview)
    protected lateinit var graphView: GraphView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_frequency_analysis, container, false)
    }

    override fun showEvaluation(method: FrequencyAnalysisMethod.Method, xData: DoubleArray, yData: DoubleArray) {

        if (xData.size != yData.size) {
            return
        }

        val list: ArrayList<DataPoint> = arrayListOf()
        (0..xData.size).mapIndexedTo(list) {idx,_ -> DataPoint(xData[idx], yData[idx]) }
        val series = BarGraphSeries(list.toTypedArray())
        graphView.addSeries(series)
    }

    override fun setupViews() {

        graphView.viewport.borderColor = Color.WHITE
        graphView.gridLabelRenderer.gridColor = Color.WHITE
        graphView.gridLabelRenderer.horizontalAxisTitleColor = Color.WHITE
        graphView.gridLabelRenderer.verticalAxisTitleColor = Color.WHITE
        graphView.gridLabelRenderer.horizontalLabelsColor = Color.WHITE
        graphView.gridLabelRenderer.verticalLabelsColor = Color.WHITE
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