package gr.cpaleop.public_announcements.domain.repositories

import gr.cpaleop.core.domain.entities.Announcement
import kotlinx.coroutines.flow.Flow

interface AnnouncementsRepository {

    suspend fun getPublicAnnouncementsFlow(): Flow<List<Announcement>>
}