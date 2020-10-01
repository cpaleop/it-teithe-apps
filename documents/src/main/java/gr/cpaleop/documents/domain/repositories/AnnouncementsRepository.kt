package gr.cpaleop.documents.domain.repositories

interface AnnouncementsRepository {

    suspend fun getAnnouncementTitleById(announcementId: String): String
}