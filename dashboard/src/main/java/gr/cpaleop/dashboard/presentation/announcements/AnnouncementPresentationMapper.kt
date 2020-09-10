package gr.cpaleop.dashboard.presentation.announcements

import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.core.presentation.DateFormatter

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