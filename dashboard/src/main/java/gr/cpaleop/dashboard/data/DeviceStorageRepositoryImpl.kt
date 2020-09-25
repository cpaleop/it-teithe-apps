package gr.cpaleop.dashboard.data

import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.dashboard.domain.entities.AnnouncementFolder
import gr.cpaleop.dashboard.domain.repositories.AnnouncementsRepository
import gr.cpaleop.dashboard.domain.repositories.DeviceStorageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DeviceStorageRepositoryImpl(
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val deviceStorageRepository: gr.cpaleop.core.domain.repositories.DeviceStorageRepository,
    private val announcementsRepository: AnnouncementsRepository
) : DeviceStorageRepository {

    override suspend fun getDocuments(): List<Document> {
        return deviceStorageRepository.getDocuments()
    }

    override suspend fun getDocumentsByAnnouncementId(announcementId: String): List<Document> =
        withContext(ioDispatcher) {
            deviceStorageRepository.getDocumentsByAnnouncementId(announcementId)
        }

    override suspend fun getAnnouncementFolders(): List<AnnouncementFolder> =
        withContext(ioDispatcher) {
            val announcementFolders = deviceStorageRepository.getDocuments().map { document ->
                AnnouncementFolder(
                    id = document.announcementId,
                    title = announcementsRepository.getAnnouncementTitleById(document.announcementId),
                    lastModified = document.lastModified
                )
            }
            return@withContext announcementFolders/*.distinctBy { announcementFolder ->
                announcementFolder.id
            }*/
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