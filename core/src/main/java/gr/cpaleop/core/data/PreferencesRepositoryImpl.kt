package gr.cpaleop.core.data

import android.content.Context
import gr.cpaleop.core.domain.repositories.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PreferencesRepositoryImpl(private val applicationContext: Context) : PreferencesRepository {

    private val sharedPreferences =
        applicationContext.getSharedPreferences(
            PreferencesRepository.PREFERENCES_FILE_KEY,
            Context.MODE_PRIVATE
        )

    override fun putStringAsync(key: String?, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override suspend fun putString(key: String?, value: String?) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putString(key, value).commit()
        }
    }

    override fun getString(key: String?): String? {
        return sharedPreferences.getString(key, "")
    }

    override fun putBooleanAsync(key: String?, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    override suspend fun putBoolean(key: String?, value: Boolean) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putBoolean(key, value).apply()
        }
    }

    override fun getBoolean(key: String?): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    override fun putIntAsync(key: String?, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    override suspend fun putInt(key: String?, value: Int) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putInt(key, value).apply()
        }
    }

    override fun getInt(key: String?): Int {
        return sharedPreferences.getInt(key, -1)
    }
}