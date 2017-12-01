package at.fhooe.mc.emg.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.core.analysis.FrequencyAnalysisMethod
import at.fhooe.mc.emg.core.analysis.FrequencyAnalysisView
import at.shockbytes.remote.fragment.BaseFragment

/**
 * @author Martin Macheiner
 * Date: 01.12.2017.
 */
class FrequencyAnalysisFragment : BaseFragment(), FrequencyAnalysisView {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_frequency_analysis, container, false)
    }

    override fun showEvaluation(method: FrequencyAnalysisMethod.Method, xData: DoubleArray, yData: DoubleArray) {
        // TODO
    }

    override fun setupViews() {
        // TODO
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