package gr.cpaleop.documents.data

import gr.cpaleop.common.extensions.mapAsync
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

            if (cachedAnnouncements.isEmpty()) {
                val remoteAnnouncementsTitles =
                    announcementsApi.fetchAnnouncementTitleById(announcementId)
                        .mapAsync { it.title ?: it.titleEn }
                announcementName = remoteAnnouncementsTitles.filterNotNull().first()
            } else {
                val firstCachedAnnouncement = cachedAnnouncements.first()
                announcementName =
                    firstCachedAnnouncement.title ?: firstCachedAnnouncement.titleEn ?: ""

            }
            return@withContext announcementName
        }
}