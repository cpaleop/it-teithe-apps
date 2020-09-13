package gr.cpaleop.dashboard.presentation.announcements

import gr.cpaleop.core.domain.DateFormatter
import gr.cpaleop.core.domain.entities.Announcement

class AnnouncementPresentationMapper(private val dateFormatter: DateFormatter) {

    operator fun invoke(announcement: Announcement): AnnouncementPresentation {
        return AnnouncementPresentation(
            id = announcement.id,
            title = announcement.title,
            date = dateFormatter(announcement.date, "dd MMMM yyyy HH:mm"),
            category = announcement.category,
            publisherName = announcement.publisherName,
            content = announcement.text
        )
    }
}