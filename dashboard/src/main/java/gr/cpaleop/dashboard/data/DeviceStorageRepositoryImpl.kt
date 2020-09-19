package gr.cpaleop.dashboard.data

import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.dashboard.domain.repositories.DeviceStorageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeviceStorageRepositoryImpl(private val deviceStorageRepository: gr.cpaleop.core.domain.repositories.DeviceStorageRepository) :
    DeviceStorageRepository {

    override suspend fun getDocuments(): List<Document> {
        return deviceStorageRepository.getDocuments()
    }

    override suspend fun getDocumentByUri(uri: String): Document =
        withContext(Dispatchers.IO) {
            deviceStorageRepository.getDocumentByUri(uri)
        }

    override suspend fun deleteDocument(uri: String) = withContext(Dispatchers.IO) {
        deviceStorageRepository.deleteDocument(uri)
    }

    override suspend fun renameDocument(uri: String, newName: String) =
        withContext(Dispatchers.IO) {
            deviceStorageRepository.renameDocument(uri, newName)
        }
}