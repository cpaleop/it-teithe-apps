package gr.cpaleop.announcements.presentation

import gr.cpaleop.core.domain.DateFormatter
import gr.cpaleop.core.domain.entities.Announcement

class AnnouncementPresentationMapperImpl(private val dateFormatter: DateFormatter) :
    AnnouncementPresentationMapper {

    override operator fun invoke(announcement: Announcement): AnnouncementPresentation {
        return AnnouncementPresentation(
            id = announcement.id,
            title = announcement.title,
            date = dateFormatter(announcement.date, "dd MMMM yyyy HH:mm"),
            category = announcement.category.name,
            publisherName = announcement.publisherName,
            content = announcement.text,
            hasAttachments = announcement.attachments.isNotEmpty()
        )
    }
}