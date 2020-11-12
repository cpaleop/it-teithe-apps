package gr.cpaleop.announcements.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import gr.cpaleop.announcements.domain.repositories.AnnouncementsRepository
import gr.cpaleop.core.data.datasources.AnnouncementsDataSource
import gr.cpaleop.core.data.mappers.AnnouncementMapper
import gr.cpaleop.core.datasource.model.local.AppDatabase
import gr.cpaleop.core.datasource.remote.AnnouncementsApi
import gr.cpaleop.core.datasource.remote.CategoriesApi
import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.domain.entities.Announcement
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class AnnouncementsRepositoryImpl(
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val announcementsApi: AnnouncementsApi,
    private val categoriesApi: CategoriesApi,
    private val appDatabase: AppDatabase,
    private val announcementMapper: AnnouncementMapper,
    private val json: Json,
    private val announcementsDataSource: AnnouncementsDataSource
) : AnnouncementsRepository {

    private var dataPagingSource: PagingSource<Int, Announcement>? = null
    private var filterQuery: String? = null

    override suspend fun invalidateDataSource() {
        dataPagingSource?.invalidate()
    }

    override suspend fun getAnnouncements(): Flow<PagingData<Announcement>> =
        withContext(ioDispatcher) {
            Pager(
                config = PagingConfig(pageSize = AnnouncementsPagingSource.PAGE_SIZE),
                pagingSourceFactory = {
                    AnnouncementsPagingSource(
                        announcementsApi,
                        categoriesApi,
                        appDatabase,
                        announcementMapper,
                        filterQuery,
                        json
                    ).also {
                        dataPagingSource = it
                        // Empty values because this class' object will be singleton.
                        filterQuery = null
                    }
                }
            ).flow
        }

    override suspend fun getAnnouncementTitleById(announcementId: String): String =
        withContext(ioDispatcher) {
            val remoteAnnouncement = announcementsDataSource.fetchAnnouncementById(announcementId)
            remoteAnnouncement.title ?: remoteAnnouncement.titleEn ?: ""
        }

    override suspend fun filter(filterQuery: String) {
        this.filterQuery = filterQuery
        dataPagingSource?.invalidate()
    }
}