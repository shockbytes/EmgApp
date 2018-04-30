package at.fhooe.mc.emg.app.view

import at.fhooe.mc.emg.core.view.EmgView
import at.fhooe.mc.emg.core.view.VisualView

/**
 * Author:  Martin Macheiner
 * Date:    28.11.2017.
 */
interface AndroidEmgView<in T> : EmgView {

    fun setVisualView(view: VisualView<T>)

}