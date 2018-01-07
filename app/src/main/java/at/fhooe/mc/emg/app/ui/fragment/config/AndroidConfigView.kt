package at.fhooe.mc.emg.app.ui.fragment.config

import android.support.annotation.LayoutRes
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import butterknife.ButterKnife
import butterknife.Unbinder

/**
 * @author Martin Macheiner
 * Date: 03.12.2017.
 */
abstract class AndroidConfigView : DialogFragment() {

    var viewReadyListener: (()-> Unit)? = null

    private var unbinder: Unbinder? = null

    protected fun buildView(@LayoutRes id: Int): View {
        val v = LayoutInflater.from(context).inflate(id, null, false)
        unbinder = ButterKnife.bind(this, v)
        viewReadyListener?.invoke()
        return v
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder?.unbind()
    }

}