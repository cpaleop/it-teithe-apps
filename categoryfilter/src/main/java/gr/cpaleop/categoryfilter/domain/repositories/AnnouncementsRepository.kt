package gr.cpaleop.categoryfilter.domain.repositories

import gr.cpaleop.categoryfilter.domain.entities.Announcement

interface AnnouncementsRepository {

    suspend fun getAnnouncementsByCategory(category: String): List<Announcement>
}