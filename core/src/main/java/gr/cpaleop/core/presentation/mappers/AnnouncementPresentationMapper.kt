package gr.cpaleop.core.presentation.mappers

import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.core.presentation.AnnouncementPresentation

interface AnnouncementPresentationMapper {

    operator fun invoke(
        announcement: Announcement,
        filterQuery: String = ""
    ): AnnouncementPresentation
}