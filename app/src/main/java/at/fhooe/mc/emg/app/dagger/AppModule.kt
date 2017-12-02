package at.fhooe.mc.emg.app.dagger

import at.fhooe.mc.emg.app.client.bluetooth.BluetoothClientDriver
import at.fhooe.mc.emg.app.core.AndroidEmgController
import at.fhooe.mc.emg.app.core.EmgApp
import at.fhooe.mc.emg.app.util.SharedPreferencesEmgConfigStorage
import at.fhooe.mc.emg.clientdriver.EmgClientDriver
import at.fhooe.mc.emg.core.client.network.NetworkClientDriver
import at.fhooe.mc.emg.core.client.simulation.SimulationClientDriver
import at.fhooe.mc.emg.core.tools.Tool
import at.fhooe.mc.emg.core.tools.conconi.ConconiTool
import dagger.Module
import dagger.Provides
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
        return arrayListOf(SimulationClientDriver(simulationFolder = simulationFolder),
                NetworkClientDriver(),
                BluetoothClientDriver())
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
        // TODO Find and create a suitable folder here
        // Can be basically only on the SD Card, because internal memory would require
        // special Android Code
        return ""
    }


}