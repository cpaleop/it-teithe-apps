package gr.cpaleop.dashboard.domain.repositories

import gr.cpaleop.core.domain.behavior.LanguageCode
import gr.cpaleop.dashboard.domain.entities.DocumentSort

interface PreferencesRepository {

    @LanguageCode
    suspend fun getPreferredLanguage(): String?

    suspend fun getDocumentSort(): DocumentSort

    suspend fun updateDocumentSort(documentSort: DocumentSort)
}