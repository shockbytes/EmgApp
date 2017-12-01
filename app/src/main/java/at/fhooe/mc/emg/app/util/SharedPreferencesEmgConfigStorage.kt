package at.fhooe.mc.emg.app.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import at.fhooe.mc.emg.core.util.config.EmgConfig
import at.fhooe.mc.emg.core.util.config.EmgConfigStorage

/**
 * @author Martin Macheiner
 * Date: 01.12.2017.
 */
class SharedPreferencesEmgConfigStorage(context: Context) : EmgConfigStorage {

    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    override fun load(): EmgConfig {
        // TODO Load EmgConfig from Shared Preferences
        return EmgConfig()
    }

    override fun store(config: EmgConfig) {
        // TODO Store EmgConfig to Shared Preferences
    }


}