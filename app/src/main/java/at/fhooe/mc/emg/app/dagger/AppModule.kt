package at.fhooe.mc.emg.app.dagger

import android.os.Environment
import at.fhooe.mc.emg.app.core.EmgApp
import at.fhooe.mc.emg.app.storage.AndroidExternalFileStorage
import at.fhooe.mc.emg.core.storage.FileStorage
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
    @Named("emgFolder")
    fun provideEmgFolder(): String {
        val path = File("${Environment.getExternalStorageDirectory().absolutePath}/emg")
        if (!path.exists()) {
            path.mkdirs()
        }
        return path.absolutePath
    }


    @Provides
    @Singleton
    @Named("simulationFolder")
    fun provideSimulationFolder(@Named("emgFolder") baseDir: String): String {
        val path = File("$baseDir/sim")
        if (!path.exists()) {
            path.mkdirs()
        }
        return path.absolutePath
    }

    @Provides
    @Singleton
    fun provideFileStorage(@Named("emgFolder") baseDir: String): FileStorage {
        return AndroidExternalFileStorage(baseDir)
    }



}