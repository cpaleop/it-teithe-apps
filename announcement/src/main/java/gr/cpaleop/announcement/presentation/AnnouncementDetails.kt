package gr.cpaleop.announcement.presentation

data class AnnouncementDetails(
    val id: String,
    val category: String,
    val publisherName: String,
    val title: String,
    val text: String,
    val date: String,
    val attachments: List<String>
)