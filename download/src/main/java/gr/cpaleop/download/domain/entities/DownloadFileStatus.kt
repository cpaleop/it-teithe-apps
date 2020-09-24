package gr.cpaleop.download.domain.entities

data class DownloadFileStatus(
    val announcementId: String,
    val downloading: Boolean
)