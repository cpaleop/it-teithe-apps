package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.dashboard.domain.repositories.DeviceStorageRepository

class GetDocumentUseCaseImpl(private val deviceStorageRepository: DeviceStorageRepository) :
    GetDocumentUseCase {

    override suspend fun invoke(uri: String): Document {
        return deviceStorageRepository.getDocumentByUri(uri)
    }
}