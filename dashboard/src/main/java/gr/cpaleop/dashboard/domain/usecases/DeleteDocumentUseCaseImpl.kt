package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.repositories.DeviceStorageRepository

class DeleteDocumentUseCaseImpl(private val deviceStorageRepository: DeviceStorageRepository) :
    DeleteDocumentUseCase {

    override suspend fun invoke(documentUri: String) {
        return deviceStorageRepository.deleteDocument(documentUri)
    }
}