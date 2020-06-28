package gr.cpaleop.dashboard.domain.entities

data class Notification(
    val id: String,
    val seen: Boolean,
    val announcement: NotificationRelatedAnnouncement
)