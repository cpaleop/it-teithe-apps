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
import gr.cpaleop.dashboard.domain.entities.AnnouncementSort
import gr.cpaleop.dashboard.domain.repositories.AnnouncementsRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
class AnnouncementsRepositoryImpl(
    private val announcementsApi: AnnouncementsApi,
    private val categoriesApi: CategoriesApi,
    private val appDatabase: AppDatabase,
    private val announcementMapper: AnnouncementMapper,
    private val gson: Gson
) : AnnouncementsRepository {

    private var dataPagingSource: PagingSource<Int, Announcement>? = null
    private val filterChannel = ConflatedBroadcastChannel("")
    private val sortChannel = ConflatedBroadcastChannel<AnnouncementSort?>()

    override fun invalidateDataSource() {
        dataPagingSource?.invalidate()
    }

    override suspend fun getAnnouncements(coroutineScope: CoroutineScope): Flow<PagingData<Announcement>> =
        withContext(Dispatchers.IO) {
            filterChannel
                .asFlow()
                .debounce(300)
                .onEach {
                    dataPagingSource?.invalidate()
                }
                .launchIn(coroutineScope)

            sortChannel
                .asFlow()
                .onEach {
                    dataPagingSource?.invalidate()
                }
                .launchIn(coroutineScope)

            Pager(
                config = PagingConfig(pageSize = AnnouncementsPagingSource.PAGE_SIZE),
                pagingSourceFactory = {
                    AnnouncementsPagingSource(
                        announcementsApi,
                        categoriesApi,
                        appDatabase,
                        announcementMapper,
                        filterChannel.valueOrNull,
                        sortChannel.valueOrNull,
                        gson
                    ).also {
                        dataPagingSource = it
                    }
                }
            ).flow
        }

    override suspend fun filter(filterQuery: String) {
        filterChannel.send(filterQuery)
    }

    override suspend fun sort(announcementSort: AnnouncementSort) {
        sortChannel.send(announcementSort)
    }
}