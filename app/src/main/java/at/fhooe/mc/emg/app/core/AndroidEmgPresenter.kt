package at.fhooe.mc.emg.app.core

import android.content.Context
import android.view.View
import at.fhooe.mc.emg.app.view.AndroidEmgView
import at.fhooe.mc.emg.app.view.AndroidVisualView
import at.fhooe.mc.emg.clientdriver.EmgClientDriver
import at.fhooe.mc.emg.core.EmgPresenter
import at.fhooe.mc.emg.core.analysis.FrequencyAnalysisMethod
import at.fhooe.mc.emg.core.filter.Filter
import at.fhooe.mc.emg.core.storage.config.EmgConfigStorage
import at.fhooe.mc.emg.core.tool.Tool
import at.fhooe.mc.emg.core.view.VisualView
import java.io.File

/**
 * Author:  Martin Macheiner
 * Date:    28.11.2017.
 */
class AndroidEmgPresenter(context: Context,
                          c: List<EmgClientDriver>,
                          t: List<Tool>,
                          f: List<Filter>,
                          fam: List<FrequencyAnalysisMethod>,
                          cs: EmgConfigStorage,
                          windowSize: Int) : EmgPresenter(c, t, f,fam, Pair(listOf(), listOf()), cs, null) {

    override fun openAcquisitionCaseDesigner() {
        // Not supported on Android
    }

    override fun openAcquisitionCaseDesignerFile(file: File) {
        // Not supported on Android
    }

    override val visualView: VisualView<View> = AndroidVisualView(context, windowSize)

    var androidEmgView: AndroidEmgView<View>? = null
        set(value) {
            super.emgView = value
            value?.setVisualView(visualView)
            start() // Automatically trigger view population when a view is set
        }

}