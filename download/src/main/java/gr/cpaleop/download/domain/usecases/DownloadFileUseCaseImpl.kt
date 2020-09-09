package gr.cpaleop.download.domain.usecases

import gr.cpaleop.download.domain.repositories.DeviceStorageRepository
import gr.cpaleop.download.domain.repositories.FileRepository

class DownloadFileUseCaseImpl(
    private val fileRepository: FileRepository,
    private val deviceStorageRepository: DeviceStorageRepository
) : DownloadFileUseCase {

    override suspend fun invoke(fileId: String) {
        val downloadedFile = fileRepository.getFile(fileId)
        deviceStorageRepository.saveFile(downloadedFile)
    }
}