package gr.cpaleop.documents.domain.usecases

import gr.cpaleop.documents.domain.repositories.DeviceStorageRepository

class RenameDocumentUseCaseImpl(private val deviceStorageRepository: DeviceStorageRepository) :
    RenameDocumentUseCase {

    override suspend fun invoke(documentUri: String, newName: String) {
        return deviceStorageRepository.renameDocument(documentUri, newName)
    }
}