package gr.cpaleop.favorites.domain.repositories

import gr.cpaleop.core.domain.entities.Announcement
import kotlinx.coroutines.flow.Flow

interface AnnouncementsRepository {

    suspend fun getFavoritesFlow(): Flow<List<Announcement>>
}