package at.fhooe.mc.emg.app.util

import android.content.Context
import android.util.TypedValue
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.clientdriver.ClientCategory
import at.fhooe.mc.emg.clientdriver.EmgClientDriver
import at.fhooe.mc.emg.core.filter.Filter
import at.fhooe.mc.emg.core.tools.Tool

/**
 * @author Martin Macheiner
 * Date: 01.12.2017.
 */
object AppUtils {

    fun dp2Pixel(context: Context, dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.resources.displayMetrics).toInt()
    }

    fun iconForClient(category: ClientCategory): Int {

        return when(category) {

            ClientCategory.SIMULATION -> R.drawable.ic_client_simulation
            ClientCategory.NETWORK -> R.drawable.ic_client_network
            ClientCategory.BLUETOOTH -> R.drawable.ic_client_bluetooth
            ClientCategory.SERIAL -> 0 // Not supported in Android
        }
    }

    fun getClientDriverByName(clients: List<EmgClientDriver>, name: String): EmgClientDriver? {
        clients.forEach {
            if (it.shortName == name) {
                return it
            }
        }
        return null
    }

    fun getClientDriverByConfigViewName(clients: List<EmgClientDriver>, cvName: String): EmgClientDriver? {
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

}