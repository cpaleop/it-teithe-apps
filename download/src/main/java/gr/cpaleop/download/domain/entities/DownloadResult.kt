package gr.cpaleop.download.domain.entities

sealed class DownloadResult {

    object Success : DownloadResult()

    object Error : DownloadResult()
}