package gr.cpaleop.categoryfilter.data

import com.google.gson.Gson
import gr.cpaleop.categoryfilter.data.mappers.AnnouncementMapper
import gr.cpaleop.categoryfilter.data.model.AnnouncementCategoryFilter
import gr.cpaleop.categoryfilter.domain.entities.Announcement
import gr.cpaleop.categoryfilter.domain.repositories.AnnouncementsRepository
import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.core.data.model.local.AppDatabase
import gr.cpaleop.core.data.remote.AnnouncementsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AnnouncementsRepositoryImpl(
    private val announcementsApi: AnnouncementsApi,
    private val announcementMapper: AnnouncementMapper,
    private val appDatabase: AppDatabase,
    private val gson: Gson
) : AnnouncementsRepository {

    override suspend fun getAnnouncementsByCategory(category: String): Flow<List<Announcement>> =
        withContext(Dispatchers.IO) {
            val filterQuery = gson.toJson(AnnouncementCategoryFilter(about = category))
            val remoteAnnouncements = announcementsApi.fetchAnnouncementsByCategory(filterQuery)
            appDatabase.remoteAnnouncementsDao().insertAll(remoteAnnouncements)
            appDatabase.remoteAnnouncementsDao().observeCategory(category)
                .map { it.mapAsync(announcementMapper::invoke) }
        }
}