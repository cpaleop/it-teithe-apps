package gr.cpaleop.core.domain.repositories

import gr.cpaleop.core.domain.entities.DocumentPreview
import gr.cpaleop.core.domain.entities.DocumentSort
import gr.cpaleop.core.domain.entities.Token
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    suspend fun updateToken(token: Token)

    fun getTokenFlow(): Flow<Token>

    suspend fun updateNightMode(nightMode: Int)

    fun getNightModeFlow(): Flow<Int>

    suspend fun updateLanguage(languageCode: String)

    fun getLanguageFlow(): Flow<String>

    suspend fun updateDocumentSort(documentSort: DocumentSort)

    fun getDocumentSortFlow(): Flow<DocumentSort>

    suspend fun updateDocumentPreview(@DocumentPreview documentPreview: Int)

    fun getDocumentPreviewFlow(): Flow<Int>

    suspend fun clear()

    companion object {

        const val TOKEN_PREFERENCES_FILE_KEY = "gr.cpaleop.itteitheapps.system-settings_preferences"
        const val DOCUMENT_SORT_PREFERENCES_FILE_KEY =
            "gr.cpaleop.itteitheapps.system-settings_preferences"
        const val DOCUMENT_PREVIEW_PREFERENCES_FILE_KEY =
            "gr.cpaleop.itteitheapps.system-settings_preferences"
        const val SYSTEM_SETTINGS_PREFERENCES_FILE_KEY =
            "gr.cpaleop.itteitheapps.system-settings_preferences"
        const val PREFERENCES_FILE_KEY = "gr.cpaleop.itteitheapps.preferences"
    }
}