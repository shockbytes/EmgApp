package at.fhooe.mc.emg.app.ui.activity

import android.content.Context
import android.content.Intent
import at.fhooe.mc.emg.app.ui.activity.core.ContainerBackNavigableActivity
import at.fhooe.mc.emg.app.ui.fragment.SettingsFragment

class SettingsActivity : ContainerBackNavigableActivity() {

    override val displayFragment = SettingsFragment.newInstance()

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }
}
