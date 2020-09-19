package gr.cpaleop.dashboard.domain.repositories

import gr.cpaleop.core.domain.entities.Document

interface DeviceStorageRepository {

    suspend fun getDocuments(): List<Document>

    suspend fun getDocumentByUri(uri: String): Document

    suspend fun deleteDocument(uri: String)

    suspend fun renameDocument(uri: String, newName: String)
}