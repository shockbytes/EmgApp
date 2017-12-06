package at.fhooe.mc.emg.app.tools.conconi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.clientdriver.model.EmgData
import at.fhooe.mc.emg.core.tools.conconi.ConconiView
import at.fhooe.mc.emg.core.tools.conconi.ConconiViewCallback
import at.shockbytes.remote.fragment.BaseFragment

/**
 * @author Martin Macheiner
 * Date: 06.12.2017.
 */
// TODO Implement AndroidConconiView
class AndroidConconiView : BaseFragment(), ConconiView {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_conconi_view, container, false)
    }

    override fun setupViews() {

    }

    override fun setup(viewCallback: ConconiViewCallback) {

    }

    override fun onCountdownTick(seconds: Int) {

    }

    override fun onRoundDataAvailable(data: EmgData, round: Int) {

    }

    override fun onTick(seconds: Int, goal: Int) {

    }

}