package gr.cpaleop.announcement.data

import gr.cpaleop.announcement.domain.repositories.AnnouncementsRepository
import gr.cpaleop.core.data.model.local.AppDatabase
import gr.cpaleop.core.data.remote.AnnouncementsApi
import gr.cpaleop.core.data.remote.CategoriesApi
import gr.cpaleop.core.domain.entities.Announcement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnnouncementsRepositoryImpl(
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
}