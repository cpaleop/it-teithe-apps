package gr.cpaleop.download.domain.usecases

interface DownloadFileUseCase {

    suspend operator fun invoke(fileId: String)
}