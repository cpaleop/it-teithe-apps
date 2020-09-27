package gr.cpaleop.documents.data

import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.documents.domain.entities.DocumentSort
import gr.cpaleop.documents.domain.repositories.PreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PreferencesRepositoryImpl(
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val preferencesRepository: gr.cpaleop.core.domain.repositories.PreferencesRepository
) : PreferencesRepository {

    override suspend fun getDocumentSort(): DocumentSort =
        withContext(ioDispatcher) {
            DocumentSort(
                type = preferencesRepository.getInt(gr.cpaleop.core.domain.repositories.PreferencesRepository.DOCUMENT_SORT_TYPE),
                descending = preferencesRepository.getBoolean(gr.cpaleop.core.domain.repositories.PreferencesRepository.DOCUMENT_SORT_DESCENDING),
                selected = true
            )
        }

    override suspend fun updateDocumentSort(documentSort: DocumentSort) =
        withContext(Dispatchers.IO) {
            preferencesRepository.putInt(
                gr.cpaleop.core.domain.repositories.PreferencesRepository.DOCUMENT_SORT_TYPE,
                documentSort.type
            )
            preferencesRepository.putBoolean(
                gr.cpaleop.core.domain.repositories.PreferencesRepository.DOCUMENT_SORT_DESCENDING,
                documentSort.descending
            )
        }

    override suspend fun getDocumentPreviewPreference(): Int = withContext(ioDispatcher) {
        preferencesRepository.getInt(gr.cpaleop.core.domain.repositories.PreferencesRepository.DOCUMENT_PREVIEW)
    }

    override suspend fun updateDocumentPreviewPreference(documentPreview: Int) =
        withContext(ioDispatcher) {
            preferencesRepository.putInt(
                gr.cpaleop.core.domain.repositories.PreferencesRepository.DOCUMENT_PREVIEW,
                documentPreview
            )
        }
}