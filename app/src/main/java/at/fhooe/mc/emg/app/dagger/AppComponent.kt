package at.fhooe.mc.emg.app.dagger

import at.fhooe.mc.emg.app.ui.activity.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * @author Martin Macheiner
 * Date: 29.11.2017.
 */

@Singleton
@Component(modules= [(AppModule::class), (EmgModule::class)])
interface AppComponent {

    fun inject(activity: MainActivity)

}