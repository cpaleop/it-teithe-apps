package gr.cpaleop.dashboard.data

import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.domain.repositories.PreferencesRepository.Companion.DOCUMENT_PREVIEW
import gr.cpaleop.core.domain.repositories.PreferencesRepository.Companion.DOCUMENT_SORT_DESCENDING
import gr.cpaleop.core.domain.repositories.PreferencesRepository.Companion.DOCUMENT_SORT_TYPE
import gr.cpaleop.core.domain.repositories.PreferencesRepository.Companion.LANGUAGE
import gr.cpaleop.dashboard.domain.entities.DocumentSort
import gr.cpaleop.dashboard.domain.repositories.PreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PreferencesRepositoryImpl(
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val preferencesRepository: gr.cpaleop.core.domain.repositories.PreferencesRepository
) :
    PreferencesRepository {

    override suspend fun getPreferredLanguage(): String? = withContext(ioDispatcher) {
        preferencesRepository.getString(LANGUAGE)
    }

    override suspend fun getDocumentSort(): DocumentSort = withContext(ioDispatcher) {
        DocumentSort(
            type = preferencesRepository.getInt(DOCUMENT_SORT_TYPE),
            descending = preferencesRepository.getBoolean(DOCUMENT_SORT_DESCENDING),
            selected = true
        )
    }

    override suspend fun updateDocumentSort(documentSort: DocumentSort) =
        withContext(Dispatchers.IO) {
            preferencesRepository.putInt(DOCUMENT_SORT_TYPE, documentSort.type)
            preferencesRepository.putBoolean(DOCUMENT_SORT_DESCENDING, documentSort.descending)
        }

    override suspend fun getDocumentPreviewPreference(): Int = withContext(ioDispatcher) {
        preferencesRepository.getInt(DOCUMENT_PREVIEW)
    }

    override suspend fun updateDocumentPreviewPreference(documentPreview: Int) =
        withContext(ioDispatcher) {
            preferencesRepository.putInt(DOCUMENT_PREVIEW, documentPreview)
        }
}