package gr.cpaleop.core.domain.repositories

import gr.cpaleop.core.domain.entities.Document

interface DeviceStorageRepository {

    suspend fun saveFile(announcementId: String, fileName: String, fileData: ByteArray)

    suspend fun getDocuments(): List<Document>

    suspend fun getDocumentsByAnnouncementId(announcementId: String): List<Document>

    suspend fun getDocumentByUri(uri: String): Document

    suspend fun deleteDocument(uri: String)

    suspend fun renameDocument(uri: String, newName: String)
}