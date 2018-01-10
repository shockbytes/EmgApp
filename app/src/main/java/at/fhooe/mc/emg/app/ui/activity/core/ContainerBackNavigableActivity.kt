package at.fhooe.mc.emg.app.ui.activity.core

import android.app.Fragment
import android.os.Bundle
import at.fhooe.mc.emg.app.dagger.AppComponent

/**
 * @author Martin Macheiner
 * Date: 23.12.2017.
 */
abstract class ContainerBackNavigableActivity : BackNavigableActivity() {

    abstract val displayFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentManager.beginTransaction()
                .replace(android.R.id.content, displayFragment)
                .commit()
    }

    override fun injectToGraph(appComponent: AppComponent) {
        // Do nothing, nothings needs to be injected
    }

}