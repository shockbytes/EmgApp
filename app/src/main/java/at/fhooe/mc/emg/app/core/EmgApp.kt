package at.fhooe.mc.emg.app.core

import android.app.Application
import at.fhooe.mc.emg.app.dagger.AppComponent
import at.fhooe.mc.emg.app.dagger.AppModule
import at.fhooe.mc.emg.app.dagger.DaggerAppComponent
import at.fhooe.mc.emg.app.dagger.EmgModule

/**
 * @author Martin Macheiner
 * Date: 28.11.2017.
 */
class EmgApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .emgModule(EmgModule(this))
                .build()
    }

}