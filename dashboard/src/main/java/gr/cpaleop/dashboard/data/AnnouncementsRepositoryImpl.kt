package gr.cpaleop.dashboard.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import gr.cpaleop.core.data.local.AppDatabase
import gr.cpaleop.core.data.remote.AnnouncementsApi
import gr.cpaleop.core.data.remote.CategoriesApi
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.dashboard.data.mappers.AnnouncementMapper
import gr.cpaleop.dashboard.domain.repositories.AnnouncementsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AnnouncementsRepositoryImpl(
    private val announcementsApi: AnnouncementsApi,
    private val categoriesApi: CategoriesApi,
    private val appDatabase: AppDatabase,
    private val announcementMapper: AnnouncementMapper
) : AnnouncementsRepository {

    override suspend fun getAnnouncements(): Flow<PagingData<Announcement>> =
        withContext(Dispatchers.IO) {
            Pager(
                config = PagingConfig(pageSize = AnnouncementsPagingSource.PAGE_SIZE),
                pagingSourceFactory = {
                    AnnouncementsPagingSource(
                        announcementsApi,
                        categoriesApi,
                        appDatabase,
                        announcementMapper
                    )
                }
            ).flow
        }

    override suspend fun getAnnouncementsSearchResults(query: String): List<Announcement> =
        withContext(Dispatchers.IO) {
            TODO("Implementation of AnnouncementsApi\$fetchFilteredAnnouncements")
        }
}