package at.fhooe.mc.emg.app.ui.fragment

import android.os.Bundle
import android.preference.PreferenceFragment
import at.fhooe.mc.emg.app.R

class SettingsFragment : PreferenceFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)

    }

    companion object {

        fun newInstance(): SettingsFragment {
            val fragment = SettingsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
