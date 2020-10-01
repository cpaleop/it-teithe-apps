package gr.cpaleop.core.presentation.mappers

import gr.cpaleop.core.domain.DateFormatter
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.core.presentation.AnnouncementPresentation

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