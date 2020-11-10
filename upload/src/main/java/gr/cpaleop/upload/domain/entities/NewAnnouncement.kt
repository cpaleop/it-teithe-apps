package gr.cpaleop.upload.domain.entities

data class NewAnnouncement(
    val title: MultilanguageText,
    val text: MultilanguageText,
    val category: String,
    val attachmentsUriList: List<String>
)

data class MultilanguageText(
    val en: String,
    val gr: String
)