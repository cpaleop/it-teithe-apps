package gr.cpaleop.dashboard.domain.repositories

import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.dashboard.domain.entities.AnnouncementFolder
import kotlinx.coroutines.flow.Flow

interface DeviceStorageRepository {

    suspend fun getDocumentsFlow(): Flow<List<Document>>

    suspend fun getDocumentsByAnnouncementId(announcementId: String): Flow<List<Document>>

    suspend fun getAnnouncementFoldersFlow(): Flow<List<AnnouncementFolder>>

    suspend fun getDocumentByUri(uri: String): Document

    suspend fun deleteDocument(uri: String)

    suspend fun renameDocument(uri: String, newName: String)
}