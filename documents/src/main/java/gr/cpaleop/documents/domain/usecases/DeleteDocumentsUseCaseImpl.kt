package gr.cpaleop.documents.domain.usecases

import gr.cpaleop.documents.domain.repositories.DeviceStorageRepository

class DeleteDocumentsUseCaseImpl(private val deviceStorageRepository: DeviceStorageRepository) :
    DeleteDocumentsUseCase {

    override suspend fun invoke(documentUriList: List<String>) {
        documentUriList.forEach { documentUri ->
            deviceStorageRepository.deleteDocument(documentUri)
        }
    }
}