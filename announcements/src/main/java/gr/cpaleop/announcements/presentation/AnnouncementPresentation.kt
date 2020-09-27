package gr.cpaleop.announcements.presentation

data class AnnouncementPresentation(
    val id: String,
    val date: String,
    val title: String,
    val content: String,
    val publisherName: String,
    val category: String,
    val hasAttachments: Boolean
)