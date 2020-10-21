package gr.cpaleop.documents.data

import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.domain.entities.DocumentSort
import gr.cpaleop.documents.domain.repositories.PreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PreferencesRepositoryImpl(
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val preferencesRepository: gr.cpaleop.core.domain.repositories.PreferencesRepository
) : PreferencesRepository {

    override fun getDocumentSortFlow(): Flow<DocumentSort> {
        return preferencesRepository.getDocumentSortFlow()
    }

    override suspend fun updateDocumentSort(documentSort: DocumentSort) =
        withContext(Dispatchers.IO) {
            preferencesRepository.updateDocumentSort(documentSort)
        }

    override fun getDocumentPreviewPreferenceFlow(): Flow<Int> {
        return preferencesRepository.getDocumentPreviewFlow()
    }

    override suspend fun updateDocumentPreviewPreference(documentPreview: Int) =
        withContext(ioDispatcher) {
            preferencesRepository.updateDocumentPreview(documentPreview)
        }
}