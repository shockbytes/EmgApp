package at.fhooe.mc.emg.app.util

import android.content.Context
import android.media.MediaPlayer
import android.util.TypedValue
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.clientdriver.ClientCategory
import at.fhooe.mc.emg.clientdriver.EmgClientDriver
import at.fhooe.mc.emg.core.analysis.FrequencyAnalysisMethod
import at.fhooe.mc.emg.core.filter.Filter
import at.fhooe.mc.emg.core.tool.Tool
import java.util.regex.Pattern

/**
 * Author:  Martin Macheiner
 * Date:    01.12.2017.
 */
object AppUtils {

    const val defaultWindowSize = 512
    const val bufferSpan = 500L

    const val conconiDirectory = "/conconi"

    private val ipAddressRegex = Pattern.compile(
            "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9]))")

    fun validateIpAddress(ip: String): Boolean {
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
        return clients.find { it.shortName == name }
    }

    fun getClientDriverByConfigViewName(clients: List<EmgClientDriver>,
                                        cvName: String): EmgClientDriver? {
        return clients.find { it.configView?.name == cvName }
    }

    fun getFilterByName(filter: List<Filter>, name: String): Filter? {
        return filter.find { it.name == name }
    }

    fun getToolByName(tools: List<Tool>, name: String): Tool? {
        return tools.find { it.name == name }
    }

    fun getMethodByName(methods: List<FrequencyAnalysisMethod>,
                        name: String): FrequencyAnalysisMethod? {
        return methods.find { it.name == name }
    }


    fun playSound(context: Context, soundId: Int) {
        MediaPlayer.create(context, soundId).start()
    }

}