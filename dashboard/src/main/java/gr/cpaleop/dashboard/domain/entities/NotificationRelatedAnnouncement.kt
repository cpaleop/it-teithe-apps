package gr.cpaleop.dashboard.domain.entities

data class NotificationRelatedAnnouncement(
    val id: String,
    val date: Long,
    val title: String,
    val category: String,
    val publisherName: String
)