package gr.cpaleop.profile.domain.repositories

import gr.cpaleop.core.domain.behavior.LanguageCode

interface PreferencesRepository {

    @LanguageCode
    suspend fun getPreferredLanguage(): String?
}