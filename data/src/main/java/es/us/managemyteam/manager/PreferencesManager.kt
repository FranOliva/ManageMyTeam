package es.us.managemyteam.manager

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import es.us.managemyteam.contract.PreferencesInterface


private const val KEY_SIZE = 256 // recommended
private const val PREF_NAME = "manage_my_team_preferences"
private const val MASTER_KEY_NAME = "_androidx_security_master_key_"

class PreferencesManager(appContext: Context) : PreferencesInterface {

    private val preferences = EncryptedSharedPreferences.create(
        appContext,
        PREF_NAME,
        getMasterKey(appContext),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun save(key: String, value: Any) {
        val editor = preferences.edit()
        when (value) {
            is Boolean -> editor.putBoolean(key, value)
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Float -> editor.putFloat(key, value)
            is Long -> editor.putLong(key, value)
            else -> {
                editor.putString(key, Gson().toJson(value))
            }
        }
        editor.apply()
    }

    override fun <T> get(key: String, defaultValue: T?): T? {
        val result = when (defaultValue) {
            is Boolean -> preferences.getBoolean(key, defaultValue)
            is String -> preferences.getString(key, defaultValue)
            is Int -> preferences.getInt(key, defaultValue)
            is Float -> preferences.getFloat(key, defaultValue)
            is Long -> preferences.getLong(key, defaultValue)
            else -> {
                val json = preferences.getString(key, null)
                json?.let {
                    try {
                        Gson().fromJson<T>(it, object : TypeToken<T>() {}.type) ?: defaultValue
                    } catch (ex: Exception) {
                        defaultValue
                    }
                }
            }
        }

        return (result as? T) ?: defaultValue
    }

    private fun getParameterSpec(masterKeyAlias: String) = KeyGenParameterSpec.Builder(
        masterKeyAlias,
        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    )
        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        .setKeySize(KEY_SIZE)
        .build()

    private fun getMasterKey(
        appContext: Context, spec: KeyGenParameterSpec = getParameterSpec(MASTER_KEY_NAME)
    ) = MasterKey.Builder(appContext)
        .setKeyGenParameterSpec(spec)
        .build()

}