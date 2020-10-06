package gr.cpaleop.profile.data

import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.profile.domain.repositories.PreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class PreferencesRepositoryImpl(
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val preferencesRepository: gr.cpaleop.core.domain.repositories.PreferencesRepository
) : PreferencesRepository {

    override suspend fun getPreferredLanguage(): String? = withContext(ioDispatcher) {
        preferencesRepository.getLanguageFlow().first()
    }

    override fun getPreferredThemeFlow(): Flow<Int> {
        return preferencesRepository.getNightModeFlow()
    }

    override suspend fun updatePreferredTheme(theme: Int) = withContext(ioDispatcher) {
        preferencesRepository.updateNightMode(theme)
    }

    override suspend fun clearUserData() = withContext(ioDispatcher) {
        /*preferencesRepository.clear()*/
    }
}