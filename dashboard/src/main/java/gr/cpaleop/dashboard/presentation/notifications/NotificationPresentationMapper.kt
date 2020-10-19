package gr.cpaleop.dashboard.presentation.notifications

import gr.cpaleop.common.extensions.toResultSpannableString
import gr.cpaleop.core.domain.DateFormatter
import gr.cpaleop.dashboard.domain.entities.Notification

class NotificationPresentationMapper(private val dateFormatter: DateFormatter) {

    operator fun invoke(
        notification: Notification,
        filterQuery: String = ""
    ): NotificationPresentation {
        val title = notification.announcement.title.toResultSpannableString(filterQuery)
        val publisherName =
            notification.announcement.publisherName.toResultSpannableString(filterQuery)

        return NotificationPresentation(
            id = notification.announcement.id,
            seen = notification.seen,
            date = dateFormatter(
                notification.announcement.date,
                DateFormatter.ANNOUNCEMENT_DATE_FORMAT
            ),
            title = title,
            category = notification.announcement.category,
            publisherName = publisherName
        )
    }
}