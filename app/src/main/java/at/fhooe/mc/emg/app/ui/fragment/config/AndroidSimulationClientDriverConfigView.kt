package at.fhooe.mc.emg.app.ui.fragment.config

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.app.util.AppUtils
import at.fhooe.mc.emg.clientdriver.ClientCategory
import at.fhooe.mc.emg.clientdriver.EmgClientDriver
import at.fhooe.mc.emg.clientdriver.EmgClientDriverConfigView
import at.fhooe.mc.emg.core.client.simulation.SimulationClientDriver
import at.fhooe.mc.emg.core.client.simulation.SimulationSource
import butterknife.BindView

/**
 * @author Martin Macheiner
 * Date: 03.12.2017.
 */
class AndroidSimulationClientDriverConfigView : AndroidConfigView(), EmgClientDriverConfigView {

    override val name: String = "Simulation Config"

    @BindView(R.id.fragment_client_config_sim_spinner_sources)
    protected lateinit var spinnerSources: Spinner

    @BindView(R.id.fragment_client_config_sim_cb_endless)
    protected lateinit var cbEndlessLoop: CheckBox

    @BindView(R.id.fragment_client_config_sim_btn_apply)
    protected lateinit var btnApply: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context)
                .setTitle(name)
                .setIcon(AppUtils.iconForClient(ClientCategory.SIMULATION))
                .setView(buildView(R.layout.fragment_client_config_simulation))
                .create()
    }

    override fun show(client: EmgClientDriver) {

        client as SimulationClientDriver

        spinnerSources.adapter = ArrayAdapter<String>(context, R.layout.spinner_sim_sources,
                client.simulationSources.map { s -> s.name })

        spinnerSources.setSelection(findSimulationSourceIndexByName(client.simulationSources,
                client.simulationSource?.name), true)

        cbEndlessLoop.isSelected = client.isEndlessLoopEnabled

        btnApply.setOnClickListener {
            client.simulationSource = findSimulationSourceByName(client.simulationSources,
                    spinnerSources.selectedItem as String)
            client.isEndlessLoopEnabled = cbEndlessLoop.isSelected
            dismiss()
        }
    }

    private fun findSimulationSourceByName(sources: List<SimulationSource>,
                                           name: String): SimulationSource? {
        sources.forEach {
            if (it.name == name) {
                return it
            }
        }
        return null
    }

    private fun findSimulationSourceIndexByName(sources: List<SimulationSource>,
                                                name: String?): Int {
        sources.forEachIndexed { idx, src ->
            if (src.name == name) {
                return idx
            }
        }
        return sources.size / 2
    }


}