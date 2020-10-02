package gr.cpaleop.categoryfilter.domain.repositories

import gr.cpaleop.categoryfilter.domain.entities.Announcement
import kotlinx.coroutines.flow.Flow

interface AnnouncementsRepository {

    suspend fun updateCachedAnnouncementsByCategoryFlow(category: String)

    fun getCachedAnnouncementsByCategoryFlow(category: String): Flow<List<Announcement>>
}