package gr.cpaleop.public_announcements.data

import gr.cpaleop.common.extensions.mapAsyncSuspended
import gr.cpaleop.core.data.datasources.AnnouncementsDataSource
import gr.cpaleop.core.data.datasources.CategoriesDataSource
import gr.cpaleop.core.data.mappers.AnnouncementMapper
import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.public_announcements.domain.repositories.AnnouncementsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class AnnouncementsRepositoryImpl(
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val announcementsDataSource: AnnouncementsDataSource,
    private val categoriesDataSource: CategoriesDataSource,
    private val announcementMapper: AnnouncementMapper
) : AnnouncementsRepository {

    override suspend fun getPublicAnnouncementsFlow(): Flow<List<Announcement>> =
        withContext(ioDispatcher) {
            flow {
                emit(
                    announcementsDataSource.fetchPublicAnnouncements().mapAsyncSuspended {
                        val remoteCategory = categoriesDataSource.fetchCategoryById(it.about)
                        announcementMapper(it, remoteCategory)
                    }
                )
            }
        }
}