package gr.cpaleop.core.presentation.mappers

import gr.cpaleop.common.extensions.toResultSpannableString
import gr.cpaleop.core.domain.DateFormatter
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.core.presentation.AnnouncementPresentation

class AnnouncementPresentationMapperImpl(private val dateFormatter: DateFormatter) :
    AnnouncementPresentationMapper {

    override operator fun invoke(
        announcement: Announcement,
        filterQuery: String
    ): AnnouncementPresentation {

        val title = announcement.title.toResultSpannableString(filterQuery)
        val category = announcement.category.name.toResultSpannableString(filterQuery)
        val publisherName = announcement.publisherName.toResultSpannableString(filterQuery)
        val content = announcement.text.toResultSpannableString(filterQuery)

        return AnnouncementPresentation(
            id = announcement.id,
            title = title,
            date = dateFormatter(announcement.date, DateFormatter.ANNOUNCEMENT_DATE_FORMAT),
            category = category,
            publisherName = publisherName,
            content = content,
            hasAttachments = announcement.attachments.isNotEmpty()
        )
    }
}