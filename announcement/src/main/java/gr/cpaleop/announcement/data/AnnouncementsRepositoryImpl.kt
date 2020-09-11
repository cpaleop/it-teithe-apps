package gr.cpaleop.announcement.data

import gr.cpaleop.announcement.domain.repositories.AnnouncementsRepository
import gr.cpaleop.core.data.local.AppDatabase
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
            val localRemoteAnnouncements = appDatabase.remoteAnnouncementsDao().getFromId(id)
            val remoteAnnouncement = if (localRemoteAnnouncements.isNullOrEmpty()) {
                announcementsApi.fetchAnnouncementById(id)
            } else {
                localRemoteAnnouncements.firstOrNull()
                    ?: throw IllegalArgumentException("No announcement found with id $id")
            }

            val categoryId = remoteAnnouncement.about

            val localRemoteCategories = appDatabase.remoteCategoryDao().getFromId(
                categoryId
                    ?: throw IllegalArgumentException("No announcement found with id $id")
            )

            val remoteCategory = if (localRemoteCategories.isNullOrEmpty()) {
                categoriesApi.fetchCategories().firstOrNull { it.id == categoryId }
                    ?: throw IllegalArgumentException("No category found with id $categoryId")
            } else {
                localRemoteCategories.firstOrNull()
                    ?: throw IllegalArgumentException("No category found with id $categoryId")
            }
            announcementMapper(remoteAnnouncement, remoteCategory)
        }
}