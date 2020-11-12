package gr.cpaleop.announcement.domain.repositories

import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.core.domain.entities.SavedAnnouncement
import kotlinx.coroutines.flow.Flow

interface AnnouncementsRepository {

    suspend fun getAnnouncementById(id: String): Announcement

    suspend fun getSavedAnnouncementByIdFlow(id: String): Flow<SavedAnnouncement?>

    suspend fun isAnnouncementSaved(id: String): Boolean

    suspend fun saveAnnouncement(id: String)

    suspend fun removeAnnouncement(id: String)

    suspend fun toggleAnnouncementFavorite(id: String)
}