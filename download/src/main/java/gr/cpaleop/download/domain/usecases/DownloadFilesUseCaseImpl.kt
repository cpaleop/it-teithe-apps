package gr.cpaleop.download.domain.usecases

import gr.cpaleop.core.domain.repositories.DeviceStorageRepository
import gr.cpaleop.download.domain.DownloadAnnouncementNotifier
import gr.cpaleop.download.domain.DownloadProgressNotifier
import gr.cpaleop.download.domain.entities.DownloadFileStatus
import gr.cpaleop.download.domain.entities.DownloadProgress
import gr.cpaleop.download.domain.entities.DownloadResult
import gr.cpaleop.download.domain.repositories.FileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class DownloadFilesUseCaseImpl(
    private val downloadAnnouncementNotifier: DownloadAnnouncementNotifier,
    private val downloadProgressNotifier: DownloadProgressNotifier,
    private val fileRepository: FileRepository,
    private val deviceStorageRepository: DeviceStorageRepository
) : DownloadFilesUseCase {

    override suspend fun invoke(announcementId: String, fileIdList: List<String>): DownloadResult {
        try {
            downloadAnnouncementNotifier.emit(DownloadFileStatus(announcementId, true))
            fileIdList.forEachIndexed { index, fileId ->
                val downloadedFile = fileRepository.getFile(fileId)
                deviceStorageRepository.saveFile(
                    announcementId,
                    downloadedFile.name,
                    downloadedFile.data.toByteArray()
                )
                downloadProgressNotifier.emit(DownloadProgress(fileIdList.size, index + 1))
            }
            downloadAnnouncementNotifier.emit(DownloadFileStatus(announcementId, false))
            return DownloadResult.Success
        } catch (t: Throwable) {
            downloadAnnouncementNotifier.emit(DownloadFileStatus(announcementId, false))
            return DownloadResult.Error
        }
    }
}