package gr.cpaleop.announcement.presentation

import gr.cpaleop.core.domain.entities.Announcement

interface AnnouncementDetailsMapper {

    suspend operator fun invoke(announcement: Announcement): AnnouncementDetails
}