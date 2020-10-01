package gr.cpaleop.documents.domain.usecases

import gr.cpaleop.documents.domain.repositories.DeviceStorageRepository

class DeleteDocumentUseCaseImpl(private val deviceStorageRepository: DeviceStorageRepository) :
    DeleteDocumentUseCase {

    override suspend fun invoke(documentUri: String) {
        return deviceStorageRepository.deleteDocument(documentUri)
    }
}