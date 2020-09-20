package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.repositories.DeviceStorageRepository

class RenameDocumentUseCaseImpl(private val deviceStorageRepository: DeviceStorageRepository) :
    RenameDocumentUseCase {

    override suspend fun invoke(documentUri: String, newName: String) {
        return deviceStorageRepository.renameDocument(documentUri, newName)
    }
}