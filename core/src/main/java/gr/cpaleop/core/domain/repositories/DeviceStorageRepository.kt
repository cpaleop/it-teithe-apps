package gr.cpaleop.core.domain.repositories

import gr.cpaleop.core.domain.entities.Document

interface DeviceStorageRepository {

    suspend fun saveFile(announcementId: String, fileName: String, fileData: ByteArray)

    suspend fun getLocalDocuments(): List<Document>

    suspend fun getLocalDocumentByUri(uri: String): Document
}