package gr.cpaleop.public_announcements.data

import gr.cpaleop.common.extensions.mapAsyncSuspended
import gr.cpaleop.core.data.mappers.AnnouncementMapper
import gr.cpaleop.core.data.model.local.AppDatabase
import gr.cpaleop.core.data.remote.AnnouncementsApi
import gr.cpaleop.core.data.remote.CategoriesApi
import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.public_announcements.domain.repositories.AnnouncementsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class AnnouncementsRepositoryImpl(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val announcementsApi: AnnouncementsApi,
    private val categoriesApi: CategoriesApi,
    private val appDatabase: AppDatabase,
    private val announcementMapper: AnnouncementMapper
) : AnnouncementsRepository {

    override suspend fun getPublicAnnouncementsFlow(): Flow<List<Announcement>> =
        withContext(ioDispatcher) {
            val remoteAnnouncements = announcementsApi.fetchPublicAnnouncements()
            val remoteCategories = categoriesApi.fetchPublicCategories()
            appDatabase.remoteCategoryDao().nukeAndInsertAll(remoteCategories)
            appDatabase.remoteAnnouncementsDao().nukeAndInsertAll(remoteAnnouncements)
            flow {
                emit(remoteAnnouncements.mapAsyncSuspended {
                    val remoteCategory = appDatabase.remoteCategoryDao().fetchFromId(it.about)
                    announcementMapper(it, remoteCategory)
                })
            }
        }
}