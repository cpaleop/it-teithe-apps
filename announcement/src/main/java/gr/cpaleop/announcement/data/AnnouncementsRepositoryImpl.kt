package gr.cpaleop.announcement.data

import gr.cpaleop.announcement.domain.repositories.AnnouncementsRepository
import gr.cpaleop.core.data.datasources.AnnouncementsDataSource
import gr.cpaleop.core.data.datasources.CategoriesDataSource
import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.core.domain.entities.SavedAnnouncement
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AnnouncementsRepositoryImpl(
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val announcementMapper: AnnouncementMapper,
    private val announcementsDataSource: AnnouncementsDataSource,
    private val categoriesDataSource: CategoriesDataSource
) : AnnouncementsRepository {

    override suspend fun getAnnouncementById(id: String): Announcement =
        withContext(ioDispatcher) {
            val remoteAnnouncement = announcementsDataSource.fetchAnnouncementById(id)
            val categoryId = remoteAnnouncement.about
                ?: throw IllegalArgumentException("Announcement category was null")
            val remoteCategory = categoriesDataSource.fetchCategoryById(categoryId)
            announcementMapper(remoteAnnouncement, remoteCategory)
        }

    override suspend fun getSavedAnnouncementByIdFlow(id: String): Flow<SavedAnnouncement?> {
        return announcementsDataSource.fetchSavedAnnouncementByIdFlow(id)
    }

    override suspend fun isAnnouncementSaved(id: String): Boolean = withContext(ioDispatcher) {
        return@withContext announcementsDataSource.fetchSavedAnnouncementById(id) != null
    }

    override suspend fun saveAnnouncement(id: String) = withContext(ioDispatcher) {
        val savedAnnouncement = SavedAnnouncement(
            announcementId = id,
            dateAdded = System.currentTimeMillis()
        )
        announcementsDataSource.saveAnnouncement(savedAnnouncement)
    }

    override suspend fun removeAnnouncement(id: String) = withContext(ioDispatcher) {
        val savedAnnouncement =
            announcementsDataSource.fetchSavedAnnouncementById(id) ?: return@withContext
        announcementsDataSource.removeSavedAnnouncement(savedAnnouncement)
    }

    override suspend fun toggleAnnouncementFavorite(id: String) = withContext(ioDispatcher) {
        val exists = announcementsDataSource.fetchSavedAnnouncementById(id) != null
        if (!exists) {
            // At this point we are sure we have the announcement cached
            val announcementId = announcementsDataSource.fetchAnnouncementById(id).id
            saveAnnouncement(announcementId)
        } else {
            removeAnnouncement(id)
        }
    }
}