package gr.cpaleop.download.domain.usecases

sealed class DownloadResult {
    object Success : DownloadResult()
    object Error : DownloadResult()
}