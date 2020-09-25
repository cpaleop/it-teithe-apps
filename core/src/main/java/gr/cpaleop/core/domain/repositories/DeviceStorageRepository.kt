package gr.cpaleop.core.domain.repositories

import gr.cpaleop.core.domain.entities.Document
import kotlinx.coroutines.flow.Flow

interface DeviceStorageRepository {

    suspend fun saveFile(announcementId: String, fileName: String, fileData: ByteArray)

    suspend fun getDocumentsFlow(): Flow<List<Document>>

    suspend fun getDocumentsByAnnouncementId(announcementId: String): Flow<List<Document>>

    suspend fun getDocumentByUri(uri: String): Document

    suspend fun deleteDocument(uri: String)

    suspend fun renameDocument(uri: String, newName: String)
}