package at.fhooe.mc.emg.app.dagger

import android.os.Environment
import at.fhooe.mc.emg.app.client.bluetooth.BluetoothClientDriver
import at.fhooe.mc.emg.app.core.AndroidEmgController
import at.fhooe.mc.emg.app.core.EmgApp
import at.fhooe.mc.emg.app.ui.fragment.config.AndroidBluetoothClientDriverConfigView
import at.fhooe.mc.emg.app.ui.fragment.config.AndroidNetworkClientDriverConfigView
import at.fhooe.mc.emg.app.ui.fragment.config.AndroidSimulationClientDriverConfigView
import at.fhooe.mc.emg.app.util.SharedPreferencesEmgConfigStorage
import at.fhooe.mc.emg.clientdriver.EmgClientDriver
import at.fhooe.mc.emg.core.client.network.NetworkClientDriver
import at.fhooe.mc.emg.core.client.simulation.SimulationClientDriver
import at.fhooe.mc.emg.core.tools.Tool
import at.fhooe.mc.emg.core.tools.conconi.ConconiTool
import dagger.Module
import dagger.Provides
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

/**
 * @author Martin Macheiner
 * Date: 29.11.2017.
 */

@Module
class AppModule(private val app: EmgApp) {

    @Provides
    @Singleton
    fun provideEmgController(clients: List<@JvmSuppressWildcards EmgClientDriver>,
                             tools: List<@JvmSuppressWildcards Tool>): AndroidEmgController {
        return AndroidEmgController(app.applicationContext, clients, tools,
                SharedPreferencesEmgConfigStorage(app.applicationContext))
    }

    @Provides
    @Singleton
    fun provideEmgClients(@Named("simulationFolder") simulationFolder: String): List<@JvmSuppressWildcards EmgClientDriver> {
        return arrayListOf(
                SimulationClientDriver(AndroidSimulationClientDriverConfigView(), simulationFolder),
                NetworkClientDriver(AndroidNetworkClientDriverConfigView()),
                BluetoothClientDriver(AndroidBluetoothClientDriverConfigView()))
    }

    @Provides
    @Singleton
    fun provideTools(): List<@JvmSuppressWildcards Tool> {
        return arrayListOf(ConconiTool()
                /* This still depends on JFrame --> PeakDetectionTool() */
                )
    }

    @Provides
    @Singleton
    @Named("simulationFolder")
    fun provideSimulationFolder(): String {
        val path = File(Environment.getExternalStorageDirectory().absolutePath + "/emg/sim")
        if (!path.exists()) {
            path.mkdirs()
        }
        return path.absolutePath
    }


}