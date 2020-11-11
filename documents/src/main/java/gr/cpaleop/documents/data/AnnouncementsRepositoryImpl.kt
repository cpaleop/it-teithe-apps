package gr.cpaleop.documents.data

import gr.cpaleop.core.data.model.local.AppDatabase
import gr.cpaleop.core.data.remote.AnnouncementsApi
import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.documents.domain.repositories.AnnouncementsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AnnouncementsRepositoryImpl(
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val announcementsApi: AnnouncementsApi,
    private val appDatabase: AppDatabase,
) : AnnouncementsRepository {

    override suspend fun getAnnouncementTitleById(announcementId: String): String =
        withContext(ioDispatcher) {
            var announcementName = ""
            val cachedAnnouncements =
                appDatabase.remoteAnnouncementsDao().fetchFromId(announcementId)

            announcementName = if (cachedAnnouncements.isEmpty()) {
                val remoteAnnouncement = announcementsApi.fetchAnnouncementTitleById(announcementId)
                remoteAnnouncement.title ?: remoteAnnouncement.titleEn ?: ""
            } else {
                val firstCachedAnnouncement = cachedAnnouncements.first()
                firstCachedAnnouncement.title ?: firstCachedAnnouncement.titleEn ?: ""

            }
            return@withContext announcementName
        }
}