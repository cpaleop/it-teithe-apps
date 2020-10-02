package gr.cpaleop.categoryfilter.data

import com.google.gson.Gson
import gr.cpaleop.categoryfilter.data.mappers.AnnouncementMapper
import gr.cpaleop.categoryfilter.data.model.AnnouncementCategoryFilter
import gr.cpaleop.categoryfilter.domain.entities.Announcement
import gr.cpaleop.categoryfilter.domain.repositories.AnnouncementsRepository
import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.core.data.model.local.AppDatabase
import gr.cpaleop.core.data.remote.AnnouncementsApi
import gr.cpaleop.core.dispatchers.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class AnnouncementsRepositoryImpl(
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val announcementsApi: AnnouncementsApi,
    private val announcementMapper: AnnouncementMapper,
    private val appDatabase: AppDatabase,
    private val gson: Gson
) : AnnouncementsRepository {

    override suspend fun updateCachedAnnouncementsByCategoryFlow(category: String) =
        withContext(ioDispatcher) {
            val filterQuery = gson.toJson(AnnouncementCategoryFilter(about = category))
            val remoteAnnouncements = announcementsApi.fetchAnnouncementsByCategory(filterQuery)
            appDatabase.remoteAnnouncementsDao().insertAll(remoteAnnouncements)
        }

    override fun getCachedAnnouncementsByCategoryFlow(category: String): Flow<List<Announcement>> {
        return appDatabase.remoteAnnouncementsDao().observeByCategoryId(category)
            .map { remoteAnnouncementList ->
                remoteAnnouncementList
                    .sortedByDescending { it.date }
                    .mapAsync(announcementMapper::invoke)
            }.flowOn(ioDispatcher)
    }
}