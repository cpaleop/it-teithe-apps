package gr.cpaleop.download.domain.usecases

import gr.cpaleop.download.domain.entities.DownloadResult

interface DownloadFilesUseCase {

    suspend operator fun invoke(announcementId: String, fileIdList: List<String>): DownloadResult
}