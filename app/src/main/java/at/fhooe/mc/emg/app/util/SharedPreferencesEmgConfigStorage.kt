package at.fhooe.mc.emg.app.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import at.fhooe.mc.emg.core.util.config.EmgConfig
import at.fhooe.mc.emg.core.util.config.EmgConfigStorage
import com.google.gson.Gson

/**
 * @author Martin Macheiner
 * Date: 01.12.2017.
 */
class SharedPreferencesEmgConfigStorage(context: Context) : EmgConfigStorage {

    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    private val gson: Gson = Gson()

    private val prefConfigSerializedKey = "emg_config_serialized"

    override fun load(): EmgConfig {

        var emgConfig = gson.fromJson(prefs.getString(prefConfigSerializedKey, ""),
                EmgConfig::class.java)
        if (emgConfig == null) {
            emgConfig = EmgConfig()
        }
        return emgConfig
    }

    override fun store(config: EmgConfig) {
        val str = gson.toJson(config)
        prefs.edit().putString(prefConfigSerializedKey, str).apply()
    }


}