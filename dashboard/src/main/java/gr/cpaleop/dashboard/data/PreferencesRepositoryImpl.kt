package gr.cpaleop.dashboard.data

import gr.cpaleop.core.domain.repositories.PreferencesRepository.Companion.LANGUAGE
import gr.cpaleop.dashboard.domain.repositories.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PreferencesRepositoryImpl(private val preferencesRepository: gr.cpaleop.core.domain.repositories.PreferencesRepository) :
    PreferencesRepository {

    override suspend fun getPreferredLanguage(): String? = withContext(Dispatchers.IO) {
        preferencesRepository.getString(LANGUAGE)
    }
}