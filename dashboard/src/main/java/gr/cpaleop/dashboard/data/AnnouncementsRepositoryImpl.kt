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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

@FlowPreview
@ExperimentalCoroutinesApi
class AnnouncementsRepositoryImpl(
    private val announcementsApi: AnnouncementsApi,
    private val categoriesApi: CategoriesApi,
    private val appDatabase: AppDatabase,
    private val announcementMapper: AnnouncementMapper,
    private val gson: Gson
) : AnnouncementsRepository {

    private var _dataPagingSource: PagingSource<Int, Announcement>? = null
    private val filterChannel = ConflatedBroadcastChannel("")

    init {
        filterChannel
            .asFlow()
            .debounce(300)
            .onEach { _dataPagingSource?.invalidate() }
    }

    override suspend fun getAnnouncements(): Flow<PagingData<Announcement>> =
        withContext(Dispatchers.IO) {
            Pager(
                config = PagingConfig(pageSize = AnnouncementsPagingSource.PAGE_SIZE),
                pagingSourceFactory = {
                    AnnouncementsPagingSource(
                        announcementsApi,
                        categoriesApi,
                        appDatabase,
                        announcementMapper,
                        filterChannel.value,
                        gson
                    ).also {
                        _dataPagingSource = it
                    }
                }
            ).flow
        }

    override suspend fun filterAnnouncements(filterQuery: String) {
        filterChannel.send(filterQuery)
    }
}