package at.fhooe.mc.emg.app.ui.fragment.config

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.app.client.bluetooth.AndroidBluetoothClientDriver
import at.fhooe.mc.emg.app.util.AppUtils
import at.fhooe.mc.emg.clientdriver.ClientCategory
import at.fhooe.mc.emg.clientdriver.EmgClientDriver
import at.fhooe.mc.emg.clientdriver.EmgClientDriverConfigView
import at.fhooe.mc.emg.core.util.CoreUtils
import butterknife.BindView

/**
 * @author Martin Macheiner
 * Date: 03.12.2017.
 */
class AndroidBluetoothClientDriverConfigView : AndroidConfigView(), EmgClientDriverConfigView {

    override val name: String = "Bluetooth Config"

    @BindView(R.id.fragment_client_config_bt_txt_remote_mac)
    protected lateinit var editRemoteMac: EditText

    @BindView(R.id.fragment_client_config_bt_txt_uuid)
    protected lateinit var editUuid: EditText

    @BindView(R.id.fragment_client_config_bt_btn_apply)
    protected lateinit var btnApply: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context)
                .setTitle(name)
                .setIcon(AppUtils.iconForClient(ClientCategory.BLUETOOTH))
                .setView(buildView(R.layout.fragment_client_config_bluetooth))
                .create()
    }

    override fun show(client: EmgClientDriver) {
        client as AndroidBluetoothClientDriver

        editRemoteMac.setText(client.remoteDeviceMacAddress)
        editUuid.setText(client.uuid.toString())

        btnApply.setOnClickListener {

            val remoteMac = editRemoteMac.text.toString()
            val uuid = editUuid.text.toString()
            if (CoreUtils.validateMacAddress(remoteMac)) {
                client.setBluetoothOptions(remoteMac, uuid)
                dismiss()
            } else {
                Toast.makeText(context, "Remote MAC not valid!", Toast.LENGTH_SHORT).show()
            }
        }

    }


}