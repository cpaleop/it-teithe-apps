package gr.cpaleop.announcement.presentation

data class AnnouncementDocument(
    val announcementId: String,
    val fileIdList: List<String>
)