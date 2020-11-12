package gr.cpaleop.documents.data

import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.documents.domain.entities.AnnouncementFolder
import gr.cpaleop.documents.domain.repositories.AnnouncementsRepository
import gr.cpaleop.documents.domain.repositories.DeviceStorageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DeviceStorageRepositoryImpl(
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val deviceStorageRepository: gr.cpaleop.core.domain.repositories.DeviceStorageRepository,
    private val announcementsRepository: AnnouncementsRepository
) : DeviceStorageRepository {

    override suspend fun getDocumentsFlow(): Flow<List<Document>> {
        return deviceStorageRepository.getDocumentsFlow()
    }

    override suspend fun getDocumentsByAnnouncementId(announcementId: String): Flow<List<Document>> =
        withContext(ioDispatcher) {
            deviceStorageRepository.getDocumentsByAnnouncementId(announcementId)
        }

    override suspend fun getAnnouncementFoldersFlow(): Flow<List<AnnouncementFolder>> =
        withContext(ioDispatcher) {
            return@withContext deviceStorageRepository.getDocumentsFlow().map { documentList ->
                documentList.map { document ->
                    AnnouncementFolder(
                        id = document.announcementId,
                        title = announcementsRepository.getAnnouncementTitleById(document.announcementId),
                        lastModified = document.lastModified
                    )
                }
            }
        }

    override suspend fun getDocumentByUri(uri: String): Document =
        withContext(ioDispatcher) {
            deviceStorageRepository.getDocumentByUri(uri)
        }

    override suspend fun deleteDocument(uri: String) = withContext(ioDispatcher) {
        deviceStorageRepository.deleteDocument(uri)
    }

    override suspend fun renameDocument(uri: String, newName: String) =
        withContext(ioDispatcher) {
            deviceStorageRepository.renameDocument(uri, newName)
        }
}