package gr.cpaleop.download.domain.usecases

interface DownloadFilesUseCase {

    suspend operator fun invoke(announcementId: String, fileIdList: List<String>): DownloadResult
}