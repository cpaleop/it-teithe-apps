package gr.cpaleop.categoryfilter.data

import gr.cpaleop.categoryfilter.data.model.AnnouncementCategoryFilter
import gr.cpaleop.categoryfilter.domain.repositories.AnnouncementsRepository
import gr.cpaleop.common.extensions.mapAsyncSuspended
import gr.cpaleop.core.data.mappers.AnnouncementMapper
import gr.cpaleop.core.data.model.local.AppDatabase
import gr.cpaleop.core.data.remote.AnnouncementsApi
import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.domain.entities.Announcement
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@ExperimentalCoroutinesApi
class AnnouncementsRepositoryImpl(
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val announcementsApi: AnnouncementsApi,
    private val announcementMapper: AnnouncementMapper,
    private val appDatabase: AppDatabase,
    private val json: Json
) : AnnouncementsRepository {

    override suspend fun updateCachedAnnouncementsByCategoryFlow(category: String) =
        withContext(ioDispatcher) {
            val filterQuery = json.encodeToString(AnnouncementCategoryFilter(about = category))
            val remoteAnnouncements = announcementsApi.fetchAnnouncementsByCategory(filterQuery)
            appDatabase.remoteAnnouncementsDao().insertAll(remoteAnnouncements)
        }

    override fun getCachedAnnouncementsByCategoryFlow(categoryId: String): Flow<List<Announcement>> {
        return appDatabase.remoteAnnouncementsDao().observeByCategoryId(categoryId)
            .map { remoteAnnouncementList ->
                val category = appDatabase.remoteCategoryDao().fetchFromId(categoryId)
                remoteAnnouncementList
                    .sortedByDescending { it.date }
                    .mapAsyncSuspended { announcementMapper(it, category) }
            }.flowOn(ioDispatcher)
    }
}