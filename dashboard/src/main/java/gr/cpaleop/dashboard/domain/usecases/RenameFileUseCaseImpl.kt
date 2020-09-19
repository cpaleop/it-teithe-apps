package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.repositories.DeviceStorageRepository

class RenameFileUseCaseImpl(private val deviceStorageRepository: DeviceStorageRepository) :
    RenameFileUseCase {

    override suspend fun invoke(fileUri: String, newName: String) {
        return deviceStorageRepository.renameDocument(fileUri, newName)
    }
}