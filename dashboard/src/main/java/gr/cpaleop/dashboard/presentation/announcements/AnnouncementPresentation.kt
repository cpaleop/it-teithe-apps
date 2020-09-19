package gr.cpaleop.dashboard.presentation.announcements

data class AnnouncementPresentation(
    val id: String,
    val date: String,
    val title: String,
    val content: String,
    val publisherName: String,
    val category: String,
    val hasAttachments: Boolean
)