package gr.cpaleop.categoryfilter.domain.repositories

import gr.cpaleop.core.domain.entities.Announcement
import kotlinx.coroutines.flow.Flow

interface AnnouncementsRepository {

    suspend fun updateCachedAnnouncementsByCategory(category: String)

    suspend fun getCachedAnnouncementsByCategoryFlow(categoryId: String): Flow<List<Announcement>>
}