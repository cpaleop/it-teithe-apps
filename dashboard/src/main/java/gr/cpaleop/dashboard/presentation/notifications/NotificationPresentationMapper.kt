package gr.cpaleop.dashboard.presentation.notifications

import gr.cpaleop.core.domain.DateFormatter
import gr.cpaleop.dashboard.domain.entities.Notification

class NotificationPresentationMapper(private val dateFormatter: DateFormatter) {

    operator fun invoke(notification: Notification): NotificationPresentation {
        return NotificationPresentation(
            id = notification.announcement.id,
            seen = notification.seen,
            date = dateFormatter(
                notification.announcement.date,
                DateFormatter.ANNOUNCEMENT_DATE_FORMAT
            ),
            title = notification.announcement.title,
            category = notification.announcement.category,
            publisherName = notification.announcement.publisherName
        )
    }
}