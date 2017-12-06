package at.fhooe.mc.emg.app.ui.fragment.config

import android.support.annotation.LayoutRes
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import at.fhooe.mc.emg.app.view.OnViewReadyListener
import butterknife.ButterKnife
import butterknife.Unbinder

/**
 * @author Martin Macheiner
 * Date: 03.12.2017.
 */
abstract class AndroidConfigView : DialogFragment() {

    var viewReadyListener: OnViewReadyListener? = null

    private var unbinder: Unbinder? = null

    protected fun buildView(@LayoutRes id: Int): View {
        val v = LayoutInflater.from(context).inflate(id, null, false)
        unbinder = ButterKnife.bind(this, v)
        viewReadyListener?.onReady()
        return v
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder?.unbind()
    }

}