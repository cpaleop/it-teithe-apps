package gr.cpaleop.dashboard.data

import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.domain.repositories.PreferencesRepository.Companion.LANGUAGE
import gr.cpaleop.dashboard.domain.repositories.PreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PreferencesRepositoryImpl(
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val preferencesRepository: gr.cpaleop.core.domain.repositories.PreferencesRepository
) : PreferencesRepository {

    override suspend fun getPreferredLanguage(): String? = withContext(ioDispatcher) {
        preferencesRepository.getString(LANGUAGE)
    }
}