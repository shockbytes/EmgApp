package at.fhooe.mc.emg.app.client.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import at.fhooe.mc.emg.clientdriver.EmgClientDriver
import at.fhooe.mc.emg.clientdriver.EmgClientDriverConfigView
import at.fhooe.mc.emg.messaging.EmgMessageInterpreter
import at.fhooe.mc.emg.messaging.MessageInterpreter

import at.fhooe.mc.emg.messaging.model.EmgPacket
import com.github.ivbaranov.rxbluetooth.BluetoothConnection
import com.github.ivbaranov.rxbluetooth.RxBluetooth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.*


/**
 * @author Martin Macheiner
 * Date: 29.11.2017.
 */
class AndroidBluetoothClientDriver(context: Context,
                                   cv: EmgClientDriverConfigView? = null) : EmgClientDriver(cv) {

    override val category = ClientCategory.BLUETOOTH

    override val isDataStorageEnabled = true

    override val name: String
        get() = "Bluetooth device @ $deviceInfo"

    override var msgInterpreter: MessageInterpreter<EmgPacket> = EmgMessageInterpreter(MessageInterpreter.ProtocolVersion.V1)

    override val shortName: String = "Bluetooth"

    // Those two properties can be changed in the ConfigView, therefore they aren't private
    var remoteDeviceMacAddress: String? = "22:22:20:E8:93:47"
    var uuid: UUID = UUID.fromString("5f77cdab-8f48-4784-9958-d2736f4727c5")

    private var deviceInfo: String? = null

    private val rxBluetooth = RxBluetooth(context)
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var connection: BluetoothConnection? = null

    override fun connect(successHandler: Action, errorHandler: Consumer<Throwable>) {

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothAdapter?.enable() // Enable by default if it should be disabled
        val device = getBluetoothDeviceByName()

        if (device != null) {
            rxBluetooth.observeConnectDevice(device, uuid)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        deviceInfo = it.remoteDevice.name
                        setupBluetoothConnection(it)
                        successHandler.run()
                    }, {
                        errorHandler.accept(it)
                        deviceInfo = null
                    })

        } else {
            errorHandler.accept(Throwable("Cannot find bluetooth device!"))
        }

    }

    override fun disconnect() {
        connection?.closeConnection()
    }

    override fun sendSamplingFrequencyToClient() {
        connection?.send(msgInterpreter.buildFrequencyMessage(samplingFrequency))
    }

    fun setBluetoothOptions(remoteMac: String, uuidString: String) {
        remoteDeviceMacAddress = remoteMac
        uuid = UUID.fromString(uuidString)
    }

    private fun getBluetoothDeviceByName(): BluetoothDevice? {
        return bluetoothAdapter?.getRemoteDevice(remoteDeviceMacAddress)
    }

    private fun setupBluetoothConnection(socket: BluetoothSocket) {

        connection = BluetoothConnection(socket)
        connection?.observeStringStream()
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribe ({
                    processMessage(it) // Parse the received messages here
                }, {
                    it.printStackTrace()
                    connection?.closeConnection()
                })
    }


}