package gr.cpaleop.favorites.data

import gr.cpaleop.core.data.datasources.AnnouncementsDataSource
import gr.cpaleop.core.data.datasources.CategoriesDataSource
import gr.cpaleop.core.data.mappers.AnnouncementMapper
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
    private val announcementMapper: AnnouncementMapper,
    private val announcementsDataSource: AnnouncementsDataSource,
    private val categoriesDataSource: CategoriesDataSource
) : AnnouncementsRepository {

    override suspend fun getFavoritesFlow(): Flow<List<Announcement>> = withContext(ioDispatcher) {
        announcementsDataSource.fetchSavedAnnouncementsFlow().map {
            it.map {
                val remoteCategory = categoriesDataSource.fetchCategoryById(it.about)
                announcementMapper(it, remoteCategory)
            }
        }
    }
}