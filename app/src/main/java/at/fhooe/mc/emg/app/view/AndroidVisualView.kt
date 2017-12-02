package at.fhooe.mc.emg.app.view

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.clientdriver.ChannelData
import at.fhooe.mc.emg.core.filter.Filter
import at.fhooe.mc.emg.core.view.VisualView
import com.jjoe64.graphview.GraphView

/**
 * @author Martin Macheiner
 * Date: 28.11.2017.
 */
class AndroidVisualView(private val context: Context) : VisualView<View> {

    override val dataForFrequencyAnalysis: DoubleArray
        get() = DoubleArray(10) // TODO

    override val view: View
        get() = graphView

    private lateinit var graphView: GraphView

    init {
        initialize()
    }

    override fun initialize() {

        graphView = LayoutInflater.from(context)
                .inflate(R.layout.visual_view, null, false) as GraphView

        graphView.viewport.borderColor = Color.WHITE
        graphView.gridLabelRenderer.gridColor = Color.WHITE
        graphView.gridLabelRenderer.horizontalAxisTitleColor = Color.WHITE
        graphView.gridLabelRenderer.verticalAxisTitleColor = Color.WHITE
        graphView.gridLabelRenderer.horizontalLabelsColor = Color.WHITE
        graphView.gridLabelRenderer.verticalLabelsColor = Color.WHITE

    }

    override fun reset() {
        // TODO
    }

    override fun setYMaximum(maximum: Double) {
        // TODO
    }

    override fun update(data: ChannelData, filters: List<Filter>) {
        // TODO
    }

}