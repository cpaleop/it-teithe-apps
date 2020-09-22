package gr.cpaleop.dashboard.domain.repositories

import androidx.paging.PagingData
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.dashboard.domain.entities.AnnouncementSort
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface AnnouncementsRepository {

    fun invalidateDataSource()

    suspend fun getAnnouncements(coroutineScope: CoroutineScope): Flow<PagingData<Announcement>>

    suspend fun filter(filterQuery: String)

    suspend fun sort(announcementSort: AnnouncementSort)
}