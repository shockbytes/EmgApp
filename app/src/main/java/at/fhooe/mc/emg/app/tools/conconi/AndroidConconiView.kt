package at.fhooe.mc.emg.app.tools.conconi

import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.app.ui.fragment.AndroidToolViewFragment
import at.fhooe.mc.emg.clientdriver.model.EmgData
import at.fhooe.mc.emg.core.tools.conconi.ConconiView
import at.fhooe.mc.emg.core.tools.conconi.ConconiViewCallback

/**
 * @author Martin Macheiner
 * Date: 06.12.2017.
 */
// TODO Implement AndroidConconiView
class AndroidConconiView : AndroidToolViewFragment(), ConconiView {

    override val layoutId = R.layout.fragment_conconi_view

    private var viewCallback: ConconiViewCallback? = null

    override fun setupViews() {
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

    }

    override fun onRoundDataAvailable(data: EmgData, round: Int) {

    }

    override fun onTick(seconds: Int, goal: Int) {

    }

}