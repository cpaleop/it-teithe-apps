package gr.cpaleop.teithe_apps.data

import gr.cpaleop.core.domain.repositories.PreferencesRepository.Companion.ACCESS_TOKEN
import gr.cpaleop.core.domain.repositories.PreferencesRepository.Companion.REFRESH_TOKEN
import gr.cpaleop.teithe_apps.domain.repositories.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PreferencesRepositoryImpl(private val preferencesRepository: gr.cpaleop.core.domain.repositories.PreferencesRepository) :
    PreferencesRepository {

    override suspend fun putString(key: String, value: String) {
        preferencesRepository.putStringAsync(key, value)
    }

    override suspend fun getAccessToken(): String? = withContext(Dispatchers.IO) {
        preferencesRepository.getString(ACCESS_TOKEN)
    }

    override suspend fun getRefreshToken(): String? = withContext(Dispatchers.IO) {
        preferencesRepository.getString(REFRESH_TOKEN)
    }
}