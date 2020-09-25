package gr.cpaleop.dashboard.domain.repositories

import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.dashboard.domain.entities.AnnouncementFolder

interface DeviceStorageRepository {

    suspend fun getDocuments(): List<Document>

    suspend fun getAnnouncementFolders(): List<AnnouncementFolder>

    suspend fun getDocumentByUri(uri: String): Document

    suspend fun deleteDocument(uri: String)

    suspend fun renameDocument(uri: String, newName: String)
}