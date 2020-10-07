package gr.cpaleop.teithe_apps.data

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.DataStore
import androidx.datastore.createDataStore
import gr.cpaleop.core.domain.behavior.LanguageCode
import gr.cpaleop.core.domain.entities.DocumentPreview
import gr.cpaleop.core.domain.entities.DocumentSort
import gr.cpaleop.core.domain.entities.DocumentSortType
import gr.cpaleop.core.domain.entities.Token
import gr.cpaleop.core.domain.repositories.PreferencesRepository
import gr.cpaleop.teithe_apps.UserPreferences
import gr.cpaleop.teithe_apps.data.serializers.UserPreferencesSerializer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class PreferencesRepositoryImpl(applicationContext: Context) : PreferencesRepository {

    private val dataStore: DataStore<UserPreferences> = applicationContext.createDataStore(
        fileName = PreferencesRepository.PREFERENCES_FILE_KEY,
        serializer = UserPreferencesSerializer
    )

    override suspend fun updateToken(token: Token) {
        dataStore.updateData {
            it.toBuilder()
                .setAccessToken(token.accessToken)
                .setRefreshToken(token.refreshToken)
                .build()
        }
    }

    override fun getTokenFlow(): Flow<Token> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(UserPreferences.getDefaultInstance())
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
        dataStore.updateData {
            it.toBuilder()
                .setNightMode(nightMode)
                .build()
        }
    }

    override fun getNightModeFlow(): Flow<Int> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(UserPreferences.getDefaultInstance())
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
        dataStore.updateData {
            it.toBuilder()
                .setLanguageCode(languageCode)
                .build()
        }
    }

    override fun getLanguageFlow(): Flow<String> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(UserPreferences.getDefaultInstance())
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
        dataStore.updateData {
            it.toBuilder()
                .setDocumentSortType(documentSort.type)
                .setDocumentSortDescending(documentSort.descending)
                .build()
        }
    }

    override fun getDocumentSortFlow(): Flow<DocumentSort> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(UserPreferences.getDefaultInstance())
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
        dataStore.updateData {
            it.toBuilder()
                .setDocumentPreview(documentPreview)
                .build()
        }
    }

    override fun getDocumentPreviewFlow(): Flow<Int> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(UserPreferences.getDefaultInstance())
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
        dataStore.updateData {
            it.toBuilder().clear()
                .build()
        }
    }
}