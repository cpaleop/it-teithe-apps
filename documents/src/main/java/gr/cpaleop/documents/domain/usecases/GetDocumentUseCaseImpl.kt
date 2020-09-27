package gr.cpaleop.documents.domain.usecases

import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.documents.domain.repositories.DeviceStorageRepository

class GetDocumentUseCaseImpl(private val deviceStorageRepository: DeviceStorageRepository) :
    GetDocumentUseCase {

    override suspend fun invoke(uri: String): Document {
        return deviceStorageRepository.getDocumentByUri(uri)
    }
}