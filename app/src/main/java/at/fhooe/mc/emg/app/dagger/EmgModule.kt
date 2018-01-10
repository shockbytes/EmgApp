package at.fhooe.mc.emg.app.dagger

import at.fhooe.mc.emg.app.client.bluetooth.BluetoothClientDriver
import at.fhooe.mc.emg.app.core.AndroidEmgPresenter
import at.fhooe.mc.emg.app.core.EmgApp
import at.fhooe.mc.emg.app.tools.conconi.AndroidConconiView
import at.fhooe.mc.emg.app.tools.fatigue.AndroidMuscleFatigueView
import at.fhooe.mc.emg.app.tools.peaks.AndroidPeakDetectionView
import at.fhooe.mc.emg.app.ui.fragment.config.AndroidBluetoothClientDriverConfigView
import at.fhooe.mc.emg.app.ui.fragment.config.AndroidNetworkClientDriverConfigView
import at.fhooe.mc.emg.app.ui.fragment.config.AndroidSimulationClientDriverConfigView
import at.fhooe.mc.emg.app.util.AppUtils
import at.fhooe.mc.emg.app.util.SharedPreferencesEmgConfigStorage
import at.fhooe.mc.emg.clientdriver.EmgClientDriver
import at.fhooe.mc.emg.core.client.network.NetworkClientDriver
import at.fhooe.mc.emg.core.client.simulation.SimulationClientDriver
import at.fhooe.mc.emg.core.storage.FileStorage
import at.fhooe.mc.emg.core.tools.Tool
import at.fhooe.mc.emg.core.tools.conconi.ConconiTool
import at.fhooe.mc.emg.core.tools.fatigue.MuscleFatigueTool
import at.fhooe.mc.emg.core.tools.peaks.PeakDetectionTool
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

/**
 * @author Martin Macheiner
 * Date: 10.01.2018.
 */

@Module
class EmgModule(private val app: EmgApp) {

    @Provides
    @Singleton
    fun provideEmgController(clients: List<@JvmSuppressWildcards EmgClientDriver>,
                             tools: List<@JvmSuppressWildcards Tool>): AndroidEmgPresenter {
        return AndroidEmgPresenter(app.applicationContext, clients, tools,
                SharedPreferencesEmgConfigStorage(app.applicationContext), AppUtils.defaultWindowSize)
    }

    @Provides
    @Singleton
    fun provideEmgClients(@Named("simulationFolder") simulationFolder: String)
            : List<@JvmSuppressWildcards EmgClientDriver> {
        return arrayListOf(
                SimulationClientDriver(AndroidSimulationClientDriverConfigView(), simulationFolder),
                NetworkClientDriver(AndroidNetworkClientDriverConfigView()),
                BluetoothClientDriver(AndroidBluetoothClientDriverConfigView()))
    }

    @Provides
    @Singleton
    fun provideTools(fileStorage: FileStorage): List<@JvmSuppressWildcards Tool> {
        return arrayListOf(ConconiTool(AndroidConconiView(), fileStorage),
                PeakDetectionTool(AndroidPeakDetectionView()),
                MuscleFatigueTool(AndroidMuscleFatigueView()))
    }

}