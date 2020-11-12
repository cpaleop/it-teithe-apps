package gr.cpaleop.documents.data

import gr.cpaleop.core.data.datasources.AnnouncementsDataSource
import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.documents.domain.repositories.AnnouncementsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AnnouncementsRepositoryImpl(
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val announcementsDataSource: AnnouncementsDataSource
) : AnnouncementsRepository {

    override suspend fun getAnnouncementTitleById(announcementId: String): String =
        withContext(ioDispatcher) {
            val remoteAnnouncement = announcementsDataSource.fetchAnnouncementById(announcementId)
            remoteAnnouncement.title ?: remoteAnnouncement.titleEn ?: ""
        }
}