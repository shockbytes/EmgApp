package at.fhooe.mc.emg.app.view

import android.content.Context
import android.view.View
import android.widget.TextView
import at.fhooe.mc.emg.clientdriver.ChannelData
import at.fhooe.mc.emg.core.filter.Filter
import at.fhooe.mc.emg.core.view.VisualView

/**
 * @author Martin Macheiner
 * Date: 28.11.2017.
 */
class AndroidVisualView(private val context: Context) : VisualView<View> {
    override val dataForFrequencyAnalysis: DoubleArray
        get() = DoubleArray(10) // TODO

    override val view: View
        get() {
            val text = TextView(context)
            text.text = "TextView for AndroidVisualView"
            return text
        }

    init {
        initialize()
    }

    override fun initialize() {
        // TODO
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