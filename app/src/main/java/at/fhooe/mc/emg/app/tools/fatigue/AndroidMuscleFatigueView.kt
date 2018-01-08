package at.fhooe.mc.emg.app.tools.fatigue

import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.app.ui.fragment.AndroidToolViewFragment
import at.fhooe.mc.emg.core.tools.fatigue.MuscleFatigueView
import at.fhooe.mc.emg.core.tools.fatigue.MuscleFatigueViewCallback

/**
 * @author Martin Macheiner
 * Date: 08.01.2017.
 */
class AndroidMuscleFatigueView : AndroidToolViewFragment(), MuscleFatigueView {

    override val layoutId = R.layout.fragment_muscle_fatigue_view

    private var viewCallback: MuscleFatigueViewCallback? = null

    override fun setupViews() {
    }

    override fun setup(viewCallback: MuscleFatigueViewCallback, showViewImmediate: Boolean) {
        this.viewCallback = viewCallback

        if (showViewImmediate) {
            showView()
        }
    }

    override fun showView() {
        showToolView("tv-muscle-fatigue")
    }

}