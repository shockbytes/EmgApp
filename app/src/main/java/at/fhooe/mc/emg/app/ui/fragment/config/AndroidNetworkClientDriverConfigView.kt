package at.fhooe.mc.emg.app.ui.fragment.config

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.app.util.AppUtils
import at.fhooe.mc.emg.clientdriver.ClientCategory
import at.fhooe.mc.emg.clientdriver.EmgClientDriver
import at.fhooe.mc.emg.clientdriver.EmgClientDriverConfigView
import at.fhooe.mc.emg.core.client.network.NetworkClientDriver
import butterknife.BindView

/**
 * @author Martin Macheiner
 * Date: 03.12.2017.
 */
class AndroidNetworkClientDriverConfigView : AndroidConfigView(), EmgClientDriverConfigView {

    override val name: String = "Network Config"

    @BindView(R.id.fragment_client_config_network_txt_ip)
    protected lateinit var txtIp: TextView
    @BindView(R.id.fragment_client_config_network_txt_port)
    protected lateinit var txtPort: TextView
    @BindView(R.id.fragment_client_config_network_btn_apply)
    protected lateinit var btnApply: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context)
                .setTitle(name)
                .setIcon(AppUtils.iconForClient(ClientCategory.NETWORK))
                .setView(buildView(R.layout.fragment_client_config_network))
                .create()
    }


    override fun show(client: EmgClientDriver) {

        client as NetworkClientDriver

        txtIp.text = client.ip
        txtPort.text = client.port.toString()

        btnApply.setOnClickListener {

            val port = txtPort.text.toString().toInt()
            val ip = txtIp.text.toString()
            if (AppUtils.validateIAddress(ip)) {
                client.setSocketOptions(ip, port)
                dismiss()
            } else {
                Toast.makeText(context, "IP address not valid!", Toast.LENGTH_SHORT).show()
            }
        }
    }


}