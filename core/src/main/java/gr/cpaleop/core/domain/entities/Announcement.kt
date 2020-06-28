package gr.cpaleop.core.domain.entities

data class Announcement(
    val id: String,
    val category: String,
    val publisherName: String,
    val title: String,
    val text: String,
    val date: String
)