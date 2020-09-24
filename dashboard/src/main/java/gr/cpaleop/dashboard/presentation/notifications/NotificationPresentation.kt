package gr.cpaleop.dashboard.presentation.notifications

data class NotificationPresentation(
    val id: String,
    val seen: Boolean,
    val date: String,
    val title: String,
    val category: String,
    val publisherName: String
)