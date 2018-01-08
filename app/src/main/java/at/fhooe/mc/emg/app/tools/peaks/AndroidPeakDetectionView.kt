package at.fhooe.mc.emg.app.tools.peaks

import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.app.ui.fragment.AndroidToolViewFragment
import at.fhooe.mc.emg.core.tools.peaks.PeakDetectionView

/**
 * @author Martin Macheiner
 * Date: 08.01.2017.
 */
class AndroidPeakDetectionView : AndroidToolViewFragment(), PeakDetectionView {

    override val layoutId = R.layout.fragment_peak_detection_view

    override fun setupViews() {
    }

    override fun setup(viewCallback: Unit, showViewImmediate: Boolean) {

        if (showViewImmediate) {
            showView()
        }
    }

    override fun showView() {
        showToolView("tv-peak-detection")
    }

    override fun showData(xValues: IntArray, yValues: DoubleArray, peaks: IntArray) {
        // Show data
    }

}