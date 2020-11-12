package gr.cpaleop.categoryfilter.data

import gr.cpaleop.categoryfilter.domain.repositories.AnnouncementsRepository
import gr.cpaleop.core.data.datasources.AnnouncementsDataSource
import gr.cpaleop.core.data.datasources.CategoriesDataSource
import gr.cpaleop.core.data.mappers.AnnouncementMapper
import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.domain.entities.Announcement
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
    private val announcementMapper: AnnouncementMapper,
    private val announcementsDataSource: AnnouncementsDataSource,
    private val categoriesDataSource: CategoriesDataSource
) : AnnouncementsRepository {

    override suspend fun updateCachedAnnouncementsByCategory(category: String) =
        withContext(ioDispatcher) {
            announcementsDataSource.updateCachedAnnouncementsByCategoryId(category)
        }

    override suspend fun getCachedAnnouncementsByCategoryFlow(categoryId: String): Flow<List<Announcement>> {
        return announcementsDataSource.getCachedAnnouncementsByCategoryIdFlow(categoryId)
            .map { remoteAnnouncementList ->
                val category = categoriesDataSource.fetchCategoryById(categoryId)
                remoteAnnouncementList
                    .sortedByDescending { it.date }
                    .map { announcementMapper(it, category) }
            }.flowOn(ioDispatcher)
    }
}