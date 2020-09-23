package gr.cpaleop.dashboard.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.google.gson.Gson
import gr.cpaleop.core.data.model.local.AppDatabase
import gr.cpaleop.core.data.remote.AnnouncementsApi
import gr.cpaleop.core.data.remote.CategoriesApi
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.dashboard.data.mappers.AnnouncementMapper
import gr.cpaleop.dashboard.domain.repositories.AnnouncementsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AnnouncementsRepositoryImpl(
    private val announcementsApi: AnnouncementsApi,
    private val categoriesApi: CategoriesApi,
    private val appDatabase: AppDatabase,
    private val announcementMapper: AnnouncementMapper,
    private val gson: Gson
) : AnnouncementsRepository {

    private var dataPagingSource: PagingSource<Int, Announcement>? = null
    private var filterQuery: String? = null

    override suspend fun invalidateDataSource() {
        dataPagingSource?.invalidate()
    }

    override suspend fun getAnnouncements(coroutineScope: CoroutineScope): Flow<PagingData<Announcement>> =
        withContext(Dispatchers.IO) {
            Pager(
                config = PagingConfig(pageSize = AnnouncementsPagingSource.PAGE_SIZE),
                pagingSourceFactory = {
                    AnnouncementsPagingSource(
                        announcementsApi,
                        categoriesApi,
                        appDatabase,
                        announcementMapper,
                        filterQuery,
                        gson
                    ).also {
                        dataPagingSource = it
                        // Empty values because this class' object will be singleton.
                        filterQuery = null
                    }
                }
            ).flow
        }

    override suspend fun filter(filterQuery: String) {
        this.filterQuery = filterQuery
        dataPagingSource?.invalidate()
    }
}