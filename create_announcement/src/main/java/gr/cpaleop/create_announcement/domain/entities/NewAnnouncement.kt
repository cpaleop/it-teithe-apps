package gr.cpaleop.create_announcement.domain.entities

data class NewAnnouncement(
    val title: MultilanguageText,
    val text: MultilanguageText,
    val category: String,
    val attachments: List<Attachment>
)

data class MultilanguageText(
    val en: String,
    val gr: String
)