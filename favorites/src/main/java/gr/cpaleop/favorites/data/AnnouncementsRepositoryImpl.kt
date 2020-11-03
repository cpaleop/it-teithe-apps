package gr.cpaleop.favorites.data

import gr.cpaleop.common.extensions.mapAsyncSuspended
import gr.cpaleop.core.data.mappers.AnnouncementMapper
import gr.cpaleop.core.data.model.local.RemoteAnnouncementsDao
import gr.cpaleop.core.data.model.local.RemoteCategoryDao
import gr.cpaleop.core.data.model.local.SavedAnnouncementDao
import gr.cpaleop.core.data.remote.AnnouncementsApi
import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.favorites.domain.repositories.AnnouncementsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AnnouncementsRepositoryImpl(
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val announcementsApi: AnnouncementsApi,
    private val savedAnnouncementsDao: SavedAnnouncementDao,
    private val remoteAnnouncementsDao: RemoteAnnouncementsDao,
    private val remoteCategoryDao: RemoteCategoryDao,
    private val announcementMapper: AnnouncementMapper
) : AnnouncementsRepository {

    override suspend fun getFavoritesFlow(): Flow<List<Announcement>> = withContext(ioDispatcher) {
        savedAnnouncementsDao.fetchAllAsFlow().map { savedAnnouncements ->
            savedAnnouncements.mapAsyncSuspended { savedAnnouncement ->
                val cached = remoteAnnouncementsDao.fetchFromId(savedAnnouncement.announcementId)
                val remoteAnnouncement = if (cached.isEmpty()) {
                    announcementsApi.fetchAnnouncementById(savedAnnouncement.announcementId)
                } else {
                    cached.first()
                }
                val category = remoteCategoryDao.fetchFromId(remoteAnnouncement.about)
                announcementMapper(remoteAnnouncement, category)
            }
        }
    }
}