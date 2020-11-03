package gr.cpaleop.announcement.data

import gr.cpaleop.announcement.domain.repositories.AnnouncementsRepository
import gr.cpaleop.core.data.model.local.AppDatabase
import gr.cpaleop.core.data.remote.AnnouncementsApi
import gr.cpaleop.core.data.remote.CategoriesApi
import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.core.domain.entities.SavedAnnouncement
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AnnouncementsRepositoryImpl(
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val announcementsApi: AnnouncementsApi,
    private val categoriesApi: CategoriesApi,
    private val appDatabase: AppDatabase,
    private val announcementMapper: AnnouncementMapper
) : AnnouncementsRepository {

    override suspend fun getAnnouncementById(id: String): Announcement =
        withContext(Dispatchers.IO) {
            val cachedRemoteAnnouncements = appDatabase.remoteAnnouncementsDao().fetchFromId(id)
            val remoteAnnouncement = if (cachedRemoteAnnouncements.isNullOrEmpty()) {
                announcementsApi.fetchAnnouncementById(id)
            } else {
                cachedRemoteAnnouncements.firstOrNull()
                    ?: throw IllegalArgumentException("No announcement found with id $id")
            }

            val categoryId = remoteAnnouncement.about
                ?: throw IllegalArgumentException("Announcement category was null")

            val remoteCategory = try {
                appDatabase.remoteCategoryDao().fetchFromId(categoryId)
                    ?: throw IllegalArgumentException("No announcement found with id $id")
            } catch (t: Throwable) {
                categoriesApi.fetchCategories().firstOrNull { it.id == categoryId }
                    ?: throw IllegalArgumentException("No category found with id $categoryId")
            }
            announcementMapper(remoteAnnouncement, remoteCategory)
        }

    override fun getSavedAnnouncementByIdFlow(id: String): Flow<SavedAnnouncement?> {
        return appDatabase.savedAnnouncementDao().fetchByIdAsFlow(id)
    }

    override suspend fun isAnnouncementSaved(id: String): Boolean = withContext(ioDispatcher) {
        return@withContext appDatabase.savedAnnouncementDao().fetchById(id) != null
    }

    override suspend fun saveAnnouncement(id: String) = withContext(ioDispatcher) {
        val savedAnnouncement = SavedAnnouncement(
            announcementId = id,
            dateAdded = System.currentTimeMillis()
        )
        appDatabase.savedAnnouncementDao().insert(savedAnnouncement)
    }

    override suspend fun removeAnnouncement(id: String) = withContext(ioDispatcher) {
        val savedAnnouncement =
            appDatabase.savedAnnouncementDao().fetchById(id) ?: return@withContext
        appDatabase.savedAnnouncementDao().remove(savedAnnouncement)
    }

    override suspend fun toggleAnnouncementFavorite(id: String) = withContext(ioDispatcher) {
        val exists = appDatabase.savedAnnouncementDao().fetchById(id) != null
        if (!exists) {
            // At this point we are sure we have the announcement cached
            val announcementId = appDatabase.remoteAnnouncementsDao().fetchFromId(id).first().id
            val currentDate = System.currentTimeMillis()
            val savedAnnouncement =
                SavedAnnouncement(announcementId = announcementId, dateAdded = currentDate)
            appDatabase.savedAnnouncementDao().insert(savedAnnouncement)
        } else {
            val savedAnnouncement =
                appDatabase.savedAnnouncementDao().fetchById(id) ?: return@withContext
            appDatabase.savedAnnouncementDao().remove(savedAnnouncement)
        }
    }
}