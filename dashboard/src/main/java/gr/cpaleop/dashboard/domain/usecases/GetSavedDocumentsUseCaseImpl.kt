package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.dashboard.domain.repositories.DeviceStorageRepository

class GetSavedDocumentsUseCaseImpl(private val deviceStorageRepository: DeviceStorageRepository) :
    GetSavedDocumentsUseCase {

    override suspend fun invoke(): List<Document> {
        return deviceStorageRepository.getLocalDocuments()
    }
}