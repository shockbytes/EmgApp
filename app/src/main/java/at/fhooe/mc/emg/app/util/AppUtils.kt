package at.fhooe.mc.emg.app.util

import android.content.Context
import android.media.MediaPlayer
import android.util.TypedValue
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.clientdriver.ClientCategory
import at.fhooe.mc.emg.clientdriver.EmgClientDriver
import at.fhooe.mc.emg.core.analysis.FrequencyAnalysisMethod
import at.fhooe.mc.emg.core.filter.Filter
import at.fhooe.mc.emg.core.tools.Tool
import java.util.regex.Pattern

/**
 * @author Martin Macheiner
 * Date: 01.12.2017.
 */
object AppUtils {

    val defaultWindowSize = 512
    val bufferSpan = 500L

    val conconiDirectory = "/conconi"

    private val ipAddressRegex = Pattern.compile(
            "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9]))")

    fun validateIAddress(ip: String): Boolean {
        return ipAddressRegex.matcher(ip).matches()
    }

    fun dp2Pixel(context: Context, dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.resources.displayMetrics).toInt()
    }

    fun iconForClient(category: ClientCategory): Int {

        return when (category) {

            ClientCategory.SIMULATION -> R.drawable.ic_client_simulation
            ClientCategory.NETWORK -> R.drawable.ic_client_network
            ClientCategory.BLUETOOTH -> R.drawable.ic_client_bluetooth
            ClientCategory.SERIAL -> 0 // Not supported in Android
        }
    }

    fun getClientDriverByName(clients: List<EmgClientDriver>,
                              name: String): EmgClientDriver? {
        clients.forEach {
            if (it.shortName == name) {
                return it
            }
        }
        return null
    }

    fun getClientDriverByConfigViewName(clients: List<EmgClientDriver>,
                                        cvName: String): EmgClientDriver? {
        clients.forEach {
            if (it.configView?.name == cvName) {
                return it
            }
        }
        return null
    }

    fun getFilterByName(filter: List<Filter>, name: String): Filter? {
        filter.forEach {
            if (it.name == name) {
                return it
            }
        }
        return null
    }

    fun getToolByName(tools: List<Tool>, name: String): Tool? {
        tools.forEach {
            if (it.name == name) {
                return it
            }
        }
        return null
    }

    fun getFrequencyAnalysisTitleByMethod(method: FrequencyAnalysisMethod.Method): Int {

        return when(method) {
            FrequencyAnalysisMethod.Method.FFT -> R.string.frequency_analysis_fft
            FrequencyAnalysisMethod.Method.SPECTRUM -> R.string.frequency_analysis_spectrum
        }
    }

    fun playSound(context: Context, soundId: Int) {
        MediaPlayer.create(context, soundId).start()
    }

}