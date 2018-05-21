package at.fhooe.mc.emg.app.dagger

import at.fhooe.mc.emg.app.client.bluetooth.AndroidBluetoothClientDriver
import at.fhooe.mc.emg.app.core.AndroidEmgPresenter
import at.fhooe.mc.emg.app.core.EmgApp
import at.fhooe.mc.emg.app.tool.conconi.AndroidConconiView
import at.fhooe.mc.emg.app.tool.fatigue.AndroidMuscleFatigueView
import at.fhooe.mc.emg.app.tool.peaks.AndroidPeakDetectionView
import at.fhooe.mc.emg.app.ui.fragment.FrequencyAnalysisFragment
import at.fhooe.mc.emg.app.ui.fragment.config.AndroidBluetoothClientDriverConfigView
import at.fhooe.mc.emg.app.ui.fragment.config.AndroidMqttClientDriverConfigView
import at.fhooe.mc.emg.app.ui.fragment.config.AndroidNetworkClientDriverConfigView
import at.fhooe.mc.emg.app.ui.fragment.config.AndroidSimulationClientDriverConfigView
import at.fhooe.mc.emg.app.util.AppUtils
import at.fhooe.mc.emg.app.util.SharedPreferencesEmgConfigStorage
import at.fhooe.mc.emg.clientdriver.EmgClientDriver
import at.fhooe.mc.emg.core.analysis.FftFrequencyAnalysisMethod
import at.fhooe.mc.emg.core.analysis.FrequencyAnalysisMethod
import at.fhooe.mc.emg.core.analysis.PowerSpectrumAnalysisMethod
import at.fhooe.mc.emg.core.client.mqtt.MqttClientDriver
import at.fhooe.mc.emg.core.client.network.NetworkClientDriver
import at.fhooe.mc.emg.core.client.simulation.SimulationClientDriver
import at.fhooe.mc.emg.core.filter.*
import at.fhooe.mc.emg.core.storage.FileStorage
import at.fhooe.mc.emg.core.tool.Tool
import at.fhooe.mc.emg.core.tool.conconi.ConconiTool
import at.fhooe.mc.emg.core.tool.fatigue.MuscleFatigueTool
import at.fhooe.mc.emg.core.tool.peaks.PeakDetectionTool
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
                             tools: List<@JvmSuppressWildcards Tool>,
                             filter: List<@JvmSuppressWildcards Filter>,
                             analysisMethods: List<@JvmSuppressWildcards FrequencyAnalysisMethod>): AndroidEmgPresenter {
        return AndroidEmgPresenter(app.applicationContext, clients, tools, filter,
                analysisMethods, SharedPreferencesEmgConfigStorage(app.applicationContext),
                AppUtils.defaultWindowSize)
    }

    @Provides
    @Singleton
    fun provideEmgClients(@Named("simulationFolder") simulationFolder: String)
            : List<@JvmSuppressWildcards EmgClientDriver> {
        return listOf(
                SimulationClientDriver(AndroidSimulationClientDriverConfigView(), simulationFolder),
                NetworkClientDriver(AndroidNetworkClientDriverConfigView()),
                MqttClientDriver(AndroidMqttClientDriverConfigView()),
                AndroidBluetoothClientDriver(app.applicationContext,
                        AndroidBluetoothClientDriverConfigView()))
    }

    @Provides
    @Singleton
    fun provideTools(fileStorage: FileStorage): List<@JvmSuppressWildcards Tool> {
        return listOf(ConconiTool(AndroidConconiView(), fileStorage),
                PeakDetectionTool(AndroidPeakDetectionView()),
                MuscleFatigueTool(AndroidMuscleFatigueView()))
    }

    @Provides
    @Singleton
    fun provideFilter(): List<@JvmSuppressWildcards Filter> {
        return listOf(NoFilter(),
                LowPassFilter(),
                BandStopFilter(),
                RunningAverageFilter(),
                ThresholdFilter())
    }

    @Provides
    @Singleton
    fun provideFrequencyAnalysisMethod(): List<@JvmSuppressWildcards FrequencyAnalysisMethod> {
        return listOf(FftFrequencyAnalysisMethod(FrequencyAnalysisFragment()),
                PowerSpectrumAnalysisMethod(FrequencyAnalysisFragment()))
    }


}