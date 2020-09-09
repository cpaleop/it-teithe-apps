package gr.cpaleop.download.domain.entities

data class DownloadedFile(
    val name: String,
    val data: List<Byte>,
    val contentType: String
)