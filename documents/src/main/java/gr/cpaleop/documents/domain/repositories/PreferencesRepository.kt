package gr.cpaleop.documents.domain.repositories

import gr.cpaleop.documents.domain.entities.DocumentPreview
import gr.cpaleop.documents.domain.entities.DocumentSort

interface PreferencesRepository {

    suspend fun getDocumentSort(): DocumentSort

    suspend fun updateDocumentSort(documentSort: DocumentSort)

    @DocumentPreview
    suspend fun getDocumentPreviewPreference(): Int

    suspend fun updateDocumentPreviewPreference(@DocumentPreview documentPreview: Int)
}