package at.fhooe.mc.emg.app.tool.fatigue

import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.app.ui.fragment.AndroidToolViewFragment
import at.fhooe.mc.emg.core.tool.fatigue.MuscleFatigueToolView
import at.fhooe.mc.emg.core.tool.fatigue.MuscleFatigueToolViewCallback

/**
 * @author Martin Macheiner
 * Date: 08.01.2017.
 */
class AndroidMuscleFatigueView : AndroidToolViewFragment(), MuscleFatigueToolView {

    override val layoutId = R.layout.fragment_muscle_fatigue_view

    private var viewCallback: MuscleFatigueToolViewCallback? = null

    override fun setupViews() {
    }

    override fun setup(toolViewCallback: MuscleFatigueToolViewCallback, showViewImmediate: Boolean) {
        this.viewCallback = toolViewCallback

        if (showViewImmediate) {
            showView()
        }
    }

    override fun showView() {
        showToolView("tv-muscle-fatigue")
    }

    override fun clear() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(s: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(values: List<Pair<Double, Double>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}