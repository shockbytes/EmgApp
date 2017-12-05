package at.fhooe.mc.emg.app.ui.fragment.config

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.app.util.AppUtils
import at.fhooe.mc.emg.clientdriver.ClientCategory
import at.fhooe.mc.emg.clientdriver.EmgClientDriver
import at.fhooe.mc.emg.clientdriver.EmgClientDriverConfigView

/**
 * @author Martin Macheiner
 * Date: 03.12.2017.
 */
class AndroidBluetoothClientDriverConfigView : AndroidConfigView(), EmgClientDriverConfigView {

    override val name: String = "Bluetooth Config"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context)
                .setTitle(name)
                .setIcon(AppUtils.iconForClient(ClientCategory.BLUETOOTH))
                .setView(buildView(R.layout.fragment_client_config_bluetooth))
                .create()
    }

    override fun show(client: EmgClientDriver) {

    }


}