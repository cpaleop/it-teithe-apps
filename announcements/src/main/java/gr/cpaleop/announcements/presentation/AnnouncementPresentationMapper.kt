package gr.cpaleop.announcements.presentation

import gr.cpaleop.core.domain.entities.Announcement

interface AnnouncementPresentationMapper {

    operator fun invoke(announcement: Announcement): AnnouncementPresentation
}