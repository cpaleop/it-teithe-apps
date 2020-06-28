package gr.cpaleop.announcement.domain.repositories

import gr.cpaleop.core.domain.entities.Announcement

interface AnnouncementsRepository {

    suspend fun getAnnouncementById(id: String): Announcement
}