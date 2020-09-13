package gr.cpaleop.categoryfilter.domain.entities

data class Announcement(
    val id: String,
    val publisherName: String,
    val title: String,
    val text: String,
    val date: String,
    val attachments: List<String>
)