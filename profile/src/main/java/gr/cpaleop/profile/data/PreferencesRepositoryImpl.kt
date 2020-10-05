package gr.cpaleop.profile.data

import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.domain.repositories.PreferencesRepository.Companion.LANGUAGE
import gr.cpaleop.core.domain.repositories.PreferencesRepository.Companion.NIGHT_MODE
import gr.cpaleop.profile.domain.repositories.PreferencesRepository
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

    override suspend fun getPreferredTheme(): Int = withContext(ioDispatcher) {
        preferencesRepository.getInt(NIGHT_MODE)
    }

    override suspend fun updatePreferredTheme(theme: Int) = withContext(ioDispatcher) {
        preferencesRepository.putInt(NIGHT_MODE, theme)
    }

    override suspend fun clearUserData() = withContext(ioDispatcher) {
        preferencesRepository.clear()
    }
}