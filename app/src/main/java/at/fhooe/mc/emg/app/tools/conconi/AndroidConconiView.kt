package at.fhooe.mc.emg.app.tools.conconi

import android.util.Log
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.app.ui.fragment.BaseFragment
import at.fhooe.mc.emg.clientdriver.model.EmgData
import at.fhooe.mc.emg.core.tools.conconi.ConconiView
import at.fhooe.mc.emg.core.tools.conconi.ConconiViewCallback

/**
 * @author Martin Macheiner
 * Date: 06.12.2017.
 */
// TODO Implement AndroidConconiView
class AndroidConconiView : BaseFragment(), ConconiView {

    override val layoutId = R.layout.fragment_conconi_view

    override fun setupViews() {
        Log.wtf("Emg", "Setup views")
    }

    override fun setup(viewCallback: ConconiViewCallback) {

        Log.wtf("Emg", "Setup")

    }

    override fun onCountdownTick(seconds: Int) {

    }

    override fun onRoundDataAvailable(data: EmgData, round: Int) {

    }

    override fun onTick(seconds: Int, goal: Int) {

    }

}