package gr.cpaleop.dashboard.data

import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.dashboard.domain.repositories.DeviceStorageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeviceStorageRepositoryImpl(private val deviceStorageRepository: gr.cpaleop.core.domain.repositories.DeviceStorageRepository) :
    DeviceStorageRepository {

    override suspend fun getLocalDocuments(): List<Document> {
        return deviceStorageRepository.getLocalDocuments()
    }

    override suspend fun getLocalDocumentByUri(uri: String): Document =
        withContext(Dispatchers.IO) {
            deviceStorageRepository.getLocalDocumentByUri(uri)
        }
}