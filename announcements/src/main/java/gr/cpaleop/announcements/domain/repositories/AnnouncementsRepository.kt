package gr.cpaleop.announcements.domain.repositories

import androidx.paging.PagingData
import gr.cpaleop.core.domain.entities.Announcement
import kotlinx.coroutines.flow.Flow

interface AnnouncementsRepository {

    suspend fun invalidateDataSource()

    suspend fun getAnnouncements(): Flow<PagingData<Announcement>>

    suspend fun getAnnouncementTitleById(announcementId: String): String

    suspend fun filter(filterQuery: String)
}