package gr.cpaleop.dashboard.domain.repositories

import androidx.paging.PagingData
import gr.cpaleop.core.domain.entities.Announcement
import kotlinx.coroutines.flow.Flow

interface AnnouncementsRepository {

    suspend fun getAnnouncements(): Flow<PagingData<Announcement>>

    suspend fun filterAnnouncements(filterQuery: String)
}