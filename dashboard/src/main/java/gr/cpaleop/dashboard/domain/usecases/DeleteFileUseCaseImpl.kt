package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.repositories.DeviceStorageRepository

class DeleteFileUseCaseImpl(private val deviceStorageRepository: DeviceStorageRepository) :
    DeleteFileUseCase {

    override suspend fun invoke(fileUri: String) {
        return deviceStorageRepository.deleteDocument(fileUri)
    }
}