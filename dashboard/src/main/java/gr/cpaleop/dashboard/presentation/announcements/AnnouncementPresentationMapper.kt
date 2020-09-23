package gr.cpaleop.dashboard.presentation.announcements

import gr.cpaleop.core.domain.entities.Announcement

interface AnnouncementPresentationMapper {

    operator fun invoke(announcement: Announcement): AnnouncementPresentation
}