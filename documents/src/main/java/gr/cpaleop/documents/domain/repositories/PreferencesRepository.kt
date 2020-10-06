package gr.cpaleop.documents.domain.repositories

import gr.cpaleop.core.domain.entities.DocumentPreview
import gr.cpaleop.core.domain.entities.DocumentSort
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    fun getDocumentSortFlow(): Flow<DocumentSort>

    suspend fun updateDocumentSort(documentSort: DocumentSort)

    @DocumentPreview
    suspend fun getDocumentPreviewPreference(): Int

    suspend fun updateDocumentPreviewPreference(@DocumentPreview documentPreview: Int)
}