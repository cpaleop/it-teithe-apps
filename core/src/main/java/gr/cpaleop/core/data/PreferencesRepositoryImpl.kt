package gr.cpaleop.core.data

import android.content.Context
import gr.cpaleop.core.domain.repositories.PreferencesRepository

class PreferencesRepositoryImpl(private val applicationContext: Context) : PreferencesRepository {

    private val sharedPreferences =
        applicationContext.getSharedPreferences(
            PreferencesRepository.PREFERENCES_FILE_KEY,
            Context.MODE_PRIVATE
        )

    override fun putString(key: String?, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun getString(key: String?): String? {
        return sharedPreferences.getString(key, "")
    }

    override fun putBoolean(key: String?, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    override fun getBoolean(key: String?): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    override fun putInt(key: String?, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    override fun getInt(key: String?): Int {
        return sharedPreferences.getInt(key, -1)
    }
}