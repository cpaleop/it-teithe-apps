package gr.cpaleop.profile.domain.repositories

import gr.cpaleop.core.domain.behavior.LanguageCode
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    @LanguageCode
    suspend fun getPreferredLanguage(): String?

    fun getPreferredThemeFlow(): Flow<Int>

    suspend fun updatePreferredTheme(theme: Int)

    suspend fun clearUserData()
}