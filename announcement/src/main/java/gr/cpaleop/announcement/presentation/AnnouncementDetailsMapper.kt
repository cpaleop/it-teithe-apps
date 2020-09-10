package gr.cpaleop.announcement.presentation

import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.core.presentation.DateFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnnouncementDetailsMapper(private val dateFormatter: DateFormatter) {

    suspend operator fun invoke(announcement: Announcement): AnnouncementDetails =
        withContext(Dispatchers.Default) {
            AnnouncementDetails(
                id = announcement.id,
                title = announcement.title,
                text = announcement.text,
                category = announcement.category,
                publisherName = announcement.publisherName,
                date = dateFormatter(announcement.date, "dd MMMM yyyy HH:mm"),
                attachments = announcement.attachments
            )
        }
}