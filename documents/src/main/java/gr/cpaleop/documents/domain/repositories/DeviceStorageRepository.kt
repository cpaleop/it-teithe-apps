package gr.cpaleop.documents.domain.repositories

import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.documents.domain.entities.AnnouncementFolder
import kotlinx.coroutines.flow.Flow

interface DeviceStorageRepository {

    suspend fun getDocumentsFlow(): Flow<List<Document>>

    suspend fun getDocumentsByAnnouncementId(announcementId: String): Flow<List<Document>>

    suspend fun getAnnouncementFoldersFlow(): Flow<List<AnnouncementFolder>>

    suspend fun getDocumentByUri(uri: String): Document

    suspend fun deleteDocument(uri: String)

    suspend fun renameDocument(uri: String, newName: String)
}