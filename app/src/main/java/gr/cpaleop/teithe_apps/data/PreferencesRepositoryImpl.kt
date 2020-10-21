package gr.cpaleop.teithe_apps.data

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.DataMigration
import androidx.datastore.DataStore
import androidx.datastore.createDataStore
import gr.cpaleop.core.domain.behavior.LanguageCode
import gr.cpaleop.core.domain.entities.DocumentPreview
import gr.cpaleop.core.domain.entities.DocumentSort
import gr.cpaleop.core.domain.entities.DocumentSortType
import gr.cpaleop.core.domain.entities.Token
import gr.cpaleop.core.domain.repositories.PreferencesRepository
import gr.cpaleop.teithe_apps.*
import gr.cpaleop.teithe_apps.data.serializers.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

class TokenPreferencesMigration(private val userPreferences: DataStore<UserPreferences>) :
    DataMigration<TokenPreferences> {

    override suspend fun cleanUp() {
        //Keep empty for now
    }

    override suspend fun migrate(currentData: TokenPreferences): TokenPreferences {
        return currentData.toBuilder()
            .setAccessToken(userPreferences.data.first().accessToken)
            .setRefreshToken(userPreferences.data.first().refreshToken)
            .build()
    }

    override suspend fun shouldMigrate(currentData: TokenPreferences): Boolean {
        return (currentData.accessToken != userPreferences.data.first().accessToken) &&
                (currentData.refreshToken != userPreferences.data.first().refreshToken)
    }
}

class SystemPreferencesMigration(private val userPreferences: DataStore<UserPreferences>) :
    DataMigration<SystemPreferences> {

    override suspend fun cleanUp() {
        //Keep empty for now
    }

    override suspend fun migrate(currentData: SystemPreferences): SystemPreferences {
        return currentData.toBuilder()
            .setLanguageCode(userPreferences.data.first().languageCode)
            .setNightMode(userPreferences.data.first().nightMode)
            .build()
    }

    override suspend fun shouldMigrate(currentData: SystemPreferences): Boolean {
        return (currentData.nightMode != userPreferences.data.first().nightMode) &&
                (currentData.languageCode != userPreferences.data.first().languageCode)
    }
}

class DocumentSortPreferencesMigration(private val userPreferences: DataStore<UserPreferences>) :
    DataMigration<DocumentSortPreferences> {

    override suspend fun cleanUp() {
        //Keep empty for now
    }

    override suspend fun migrate(currentData: DocumentSortPreferences): DocumentSortPreferences {
        return currentData.toBuilder()
            .setDocumentSortDescending(userPreferences.data.first().documentSortDescending)
            .setDocumentSortType(userPreferences.data.first().documentSortType)
            .build()
    }

    override suspend fun shouldMigrate(currentData: DocumentSortPreferences): Boolean {
        return (currentData.documentSortDescending != userPreferences.data.first().documentSortDescending) &&
                (currentData.documentSortType != userPreferences.data.first().documentSortType)
    }
}

class DocumentPreviewPreferencesMigration(private val userPreferences: DataStore<UserPreferences>) :
    DataMigration<DocumentPreviewPreferences> {

    override suspend fun cleanUp() {
        //Keep empty for now
    }

    override suspend fun migrate(currentData: DocumentPreviewPreferences): DocumentPreviewPreferences {
        return currentData.toBuilder()
            .setDocumentPreview(userPreferences.data.first().documentPreview)
            .build()
    }

    override suspend fun shouldMigrate(currentData: DocumentPreviewPreferences): Boolean {
        return currentData.documentPreview != userPreferences.data.first().documentPreview
    }
}

class PreferencesRepositoryImpl(applicationContext: Context) : PreferencesRepository {

    private val userPreferencesDataStore: DataStore<UserPreferences> =
        applicationContext.createDataStore(
            fileName = PreferencesRepository.PREFERENCES_FILE_KEY,
            serializer = UserPreferencesSerializer
        )

    private val systemPreferencesDataStore: DataStore<SystemPreferences> =
        applicationContext.createDataStore(
            fileName = PreferencesRepository.SYSTEM_SETTINGS_PREFERENCES_FILE_KEY,
            serializer = SystemPreferencesSerializer,
            migrations = listOf(SystemPreferencesMigration(userPreferencesDataStore))
        )

    private val documentSortPreferencesDataStore: DataStore<DocumentSortPreferences> =
        applicationContext.createDataStore(
            fileName = PreferencesRepository.DOCUMENT_SORT_PREFERENCES_FILE_KEY,
            serializer = DocumentSortPreferencesSerializer,
            migrations = listOf(DocumentSortPreferencesMigration(userPreferencesDataStore))
        )

    private val documentPreviewPreferencesDataStore: DataStore<DocumentPreviewPreferences> =
        applicationContext.createDataStore(
            fileName = PreferencesRepository.DOCUMENT_PREVIEW_PREFERENCES_FILE_KEY,
            serializer = DocumentPreviewPreferencesSerializer,
            migrations = listOf(DocumentPreviewPreferencesMigration(userPreferencesDataStore))
        )

    private val tokenPreferencesDataStore: DataStore<TokenPreferences> =
        applicationContext.createDataStore(
            fileName = PreferencesRepository.TOKEN_PREFERENCES_FILE_KEY,
            serializer = TokenPreferencesSerializer,
            migrations = listOf(TokenPreferencesMigration(userPreferencesDataStore))
        )

    override suspend fun updateToken(token: Token) {
        tokenPreferencesDataStore.updateData {
            it.toBuilder()
                .setAccessToken(token.accessToken)
                .setRefreshToken(token.refreshToken)
                .build()
        }
    }

    override fun getTokenFlow(): Flow<Token> {
        return tokenPreferencesDataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(TokenPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }.map { preferences ->
            Token(
                accessToken = preferences.accessToken,
                refreshToken = preferences.refreshToken
            )
        }
    }

    override suspend fun updateNightMode(nightMode: Int) {
        systemPreferencesDataStore.updateData {
            it.toBuilder()
                .setNightMode(nightMode)
                .build()
        }
    }

    override fun getNightModeFlow(): Flow<Int> {
        return systemPreferencesDataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(SystemPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }.map { preferences ->
            val nightMode = preferences.nightMode
            if (nightMode != AppCompatDelegate.MODE_NIGHT_YES && nightMode != AppCompatDelegate.MODE_NIGHT_NO && nightMode != AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            else nightMode
        }
    }

    override suspend fun updateLanguage(languageCode: String) {
        systemPreferencesDataStore.updateData {
            it.toBuilder()
                .setLanguageCode(languageCode)
                .build()
        }
    }

    override fun getLanguageFlow(): Flow<String> {
        return systemPreferencesDataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(SystemPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }.map { preferences ->
            val languageCode = preferences.languageCode
            if (languageCode != LanguageCode.ENGLISH && languageCode != LanguageCode.GREEK) LanguageCode.ENGLISH
            else languageCode
        }
    }

    override suspend fun updateDocumentSort(documentSort: DocumentSort) {
        documentSortPreferencesDataStore.updateData {
            it.toBuilder()
                .setDocumentSortType(documentSort.type)
                .setDocumentSortDescending(documentSort.descending)
                .build()
        }
    }

    override fun getDocumentSortFlow(): Flow<DocumentSort> {
        return documentSortPreferencesDataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(DocumentSortPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }.map { preferences ->
            val sortType =
                if (preferences.documentSortType == 0) DocumentSortType.DATE else preferences.documentSortType
            DocumentSort(
                type = sortType,
                descending = preferences.documentSortDescending,
                selected = true
            )
        }
    }

    override suspend fun updateDocumentPreview(documentPreview: Int) {
        documentPreviewPreferencesDataStore.updateData {
            it.toBuilder()
                .setDocumentPreview(documentPreview)
                .build()
        }
    }

    override fun getDocumentPreviewFlow(): Flow<Int> {
        return documentPreviewPreferencesDataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(DocumentPreviewPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }.map { preferences ->
            val documentPreview = preferences.documentPreview
            if (documentPreview != DocumentPreview.FILE && documentPreview != DocumentPreview.FOLDER) DocumentPreview.FILE
            else documentPreview
        }
    }

    override suspend fun clear() {
        tokenPreferencesDataStore.updateData {
            it.toBuilder()
                .clear()
                .build()
        }

        systemPreferencesDataStore.updateData {
            it.toBuilder()
                .clear()
                .build()
        }

        documentPreviewPreferencesDataStore.updateData {
            it.toBuilder()
                .clear()
                .build()
        }

        documentSortPreferencesDataStore.updateData {
            it.toBuilder()
                .clear()
                .build()
        }
        userPreferencesDataStore.updateData {
            it.toBuilder()
                .clear()
                .build()
        }
    }
}