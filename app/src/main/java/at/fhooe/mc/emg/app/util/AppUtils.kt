package at.fhooe.mc.emg.app.util

import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.clientdriver.ClientCategory

/**
 * @author Martin Macheiner
 * Date: 01.12.2017.
 */
object AppUtils {

    fun iconForClient(category: ClientCategory): Int {

        return when(category) {

            ClientCategory.SIMULATION -> R.drawable.ic_client_simulation
            ClientCategory.NETWORK -> R.drawable.ic_client_network
            ClientCategory.BLUETOOTH -> R.drawable.ic_client_bluetooth
            ClientCategory.SERIAL -> 0 // Not supported in Android
        }
    }

}