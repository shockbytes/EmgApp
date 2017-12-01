package at.fhooe.mc.emg.app.core

import android.content.Context
import android.view.View
import at.fhooe.mc.emg.app.view.AndroidEmgView
import at.fhooe.mc.emg.app.view.AndroidVisualView
import at.fhooe.mc.emg.clientdriver.EmgClientDriver
import at.fhooe.mc.emg.core.EmgController
import at.fhooe.mc.emg.core.tools.Tool
import at.fhooe.mc.emg.core.util.config.EmgConfigStorage
import at.fhooe.mc.emg.core.view.VisualView

/**
 * @author Martin Macheiner
 * Date: 28.11.2017.
 */
class AndroidEmgController(context: Context, c: List<EmgClientDriver>, t: List<Tool>,
                           cs: EmgConfigStorage) : EmgController(c, t, null, cs) {

    override val visualView: VisualView<View> = AndroidVisualView(context)

    var androidEmgView: AndroidEmgView<View>? = null
        set(value) {
            super.emgView = value
            value?.setVisualView(visualView)
            start() // Automatically trigger view population when a view is set
        }

}