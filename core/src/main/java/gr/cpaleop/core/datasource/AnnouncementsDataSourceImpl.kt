package gr.cpaleop.core.datasource

import gr.cpaleop.common.extensions.mapAsyncSuspended
import gr.cpaleop.core.data.datasources.AnnouncementsDataSource
import gr.cpaleop.core.datasource.model.local.RemoteAnnouncementsDao
import gr.cpaleop.core.datasource.model.local.SavedAnnouncementDao
import gr.cpaleop.core.datasource.model.response.AnnouncementCategoryFilter
import gr.cpaleop.core.datasource.model.response.RemoteAnnouncement
import gr.cpaleop.core.datasource.remote.AnnouncementsApi
import gr.cpaleop.core.domain.entities.SavedAnnouncement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AnnouncementsDataSourceImpl(
    private val announcementsApi: AnnouncementsApi,
    private val remoteAnnouncementsDao: RemoteAnnouncementsDao,
    private val savedAnnouncementsDao: SavedAnnouncementDao,
    private val json: Json
) : AnnouncementsDataSource {

    override suspend fun fetchSavedAnnouncementsFlow(): Flow<List<RemoteAnnouncement>> {
        return savedAnnouncementsDao.fetchAllAsFlow().map { savedAnnouncements ->
            savedAnnouncements.mapAsyncSuspended { savedAnnouncement ->
                val cached = remoteAnnouncementsDao.fetchFromId(savedAnnouncement.announcementId)
                return@mapAsyncSuspended if (cached.isEmpty()) {
                    announcementsApi.fetchAnnouncementById(savedAnnouncement.announcementId)
                } else {
                    cached.first()
                }
            }
        }
    }

    override suspend fun fetchSavedAnnouncementByIdFlow(id: String): Flow<SavedAnnouncement?> {
        return savedAnnouncementsDao.fetchByIdAsFlow(id)
    }

    override suspend fun fetchSavedAnnouncementById(id: String): SavedAnnouncement? {
        return savedAnnouncementsDao.fetchById(id)
    }

    override suspend fun saveAnnouncement(savedAnnouncement: SavedAnnouncement) {
        savedAnnouncementsDao.insert(savedAnnouncement)
    }

    override suspend fun removeSavedAnnouncement(savedAnnouncement: SavedAnnouncement) {
        savedAnnouncementsDao.remove(savedAnnouncement)
    }

    override suspend fun fetchPublicAnnouncements(): List<RemoteAnnouncement> {
        val remoteAnnouncements = announcementsApi.fetchPublicAnnouncements()
        remoteAnnouncementsDao.nukeAndInsertAll(remoteAnnouncements)
        return remoteAnnouncements
    }

    override suspend fun fetchAnnouncementById(id: String): RemoteAnnouncement {
        val cachedAnnouncements =
            remoteAnnouncementsDao.fetchFromId(id)

        val remoteAnnouncement = if (cachedAnnouncements.isEmpty()) {
            announcementsApi.fetchAnnouncementById(id)
        } else {
            cachedAnnouncements.first()
        }
        return remoteAnnouncement
    }

    override suspend fun updateCachedAnnouncementsByCategoryId(categoryId: String) {
        val filterQuery = json.encodeToString(AnnouncementCategoryFilter(about = categoryId))
        val remoteAnnouncements = announcementsApi.fetchAnnouncementsByCategory(filterQuery)
        remoteAnnouncementsDao.insertAll(remoteAnnouncements)
    }

    override suspend fun getCachedAnnouncementsByCategoryIdFlow(categoryId: String): Flow<List<RemoteAnnouncement>> {
        return remoteAnnouncementsDao.observeByCategoryId(categoryId)
    }

    override suspend fun createAnnouncement(
        files: List<MultipartBody.Part?>?,
        title: RequestBody,
        titleEn: RequestBody,
        text: RequestBody,
        textEn: RequestBody,
        about: RequestBody
    ) {
        announcementsApi.createAnnouncement(
            files = files,
            title = title,
            titleEn = titleEn,
            text = text,
            textEn = textEn,
            about = about
        )
    }
}