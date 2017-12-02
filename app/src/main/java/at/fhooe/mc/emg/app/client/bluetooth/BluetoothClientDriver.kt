package at.fhooe.mc.emg.app.client.bluetooth

import at.fhooe.mc.emg.clientdriver.ClientCategory
import at.fhooe.mc.emg.clientdriver.EmgClientDriver
import at.fhooe.mc.emg.clientdriver.EmgClientDriverConfigView
import at.fhooe.mc.emg.messaging.EmgMessaging

/**
 * @author Martin Macheiner
 * Date: 29.11.2017.
 */
// TODO Implement BluetoothClientDriver
class BluetoothClientDriver(cv: EmgClientDriverConfigView? = null) : EmgClientDriver(cv) {

    override val category: ClientCategory = ClientCategory.BLUETOOTH

    override val isDataStorageEnabled: Boolean = true

    override val name: String
        get() = "Bluetooth" // + connected device

    override val protocolVersion: EmgMessaging.ProtocolVersion = EmgMessaging.ProtocolVersion.V1

    override val shortName: String = "Bluetooth"

    override fun connect() {
        // Connect to Bluetooth client
    }

    override fun disconnect() {
        // Disconnect from Bluetooth client
    }

    override fun sendSamplingFrequencyToClient() {
        // Send the sampling rate
    }

}