package at.fhooe.mc.emg.app.ui.fragment.config

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.clientdriver.EmgClientDriver
import at.fhooe.mc.emg.clientdriver.EmgClientDriverConfigView

/**
 * @author Martin Macheiner
 * Date: 03.12.2017.
 */
class AndroidNetworkClientDriverConfigView : DialogFragment(), EmgClientDriverConfigView {

    override val name: String = "Network config"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context)
                .setView(buildView())
                .create()
    }

    private fun buildView(): View {
        val v = LayoutInflater.from(context)
                .inflate(R.layout.fragment_client_config_network, null, false)
        return v
    }

    override fun show(client: EmgClientDriver) {


    }


}