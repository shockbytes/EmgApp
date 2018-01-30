package at.fhooe.mc.emg.app.tools.peaks

import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.app.ui.fragment.AndroidToolViewFragment
import at.fhooe.mc.emg.core.tools.peaks.Peak
import at.fhooe.mc.emg.core.tools.peaks.PeakDetectionView
import at.fhooe.mc.emg.core.tools.peaks.PeakDetectionViewCallback

/**
 * @author Martin Macheiner
 * Date: 08.01.2017.
 */
class AndroidPeakDetectionView : AndroidToolViewFragment(), PeakDetectionView {

    private var viewCallback: PeakDetectionViewCallback? = null

    override val layoutId = R.layout.fragment_peak_detection_view

    override fun setupViews() {
    }

    override fun showView() {
        showToolView("tv-peak-detection")
    }


    override fun setup(viewCallback: PeakDetectionViewCallback, showViewImmediate: Boolean) {
        this.viewCallback = viewCallback

        if (showViewImmediate) {
            showView()
        }
    }

    override fun showError(cause: String, title: String) {
        showToast("$title\n$cause", true)
    }

    override fun showPeaksDetail(peaks: List<Peak>) {
        // TODO
    }

    override fun showPlotData(xValues: DoubleArray, yValues: DoubleArray, xValuesPeaks: DoubleArray, yValuesPeaks: DoubleArray) {
        // TODO
    }

}