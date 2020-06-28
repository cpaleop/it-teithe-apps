package gr.cpaleop.dashboard.presentation.notifications

import gr.cpaleop.core.presentation.DateFormatter
import gr.cpaleop.dashboard.domain.entities.Notification
import gr.cpaleop.teithe_apps.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotificationPresentationMapper(private val dateFormatter: DateFormatter) {

    suspend operator fun invoke(notification: Notification): NotificationPresentation =
        withContext(Dispatchers.Default) {
            val backgroundDrawable =
                if (notification.seen) R.drawable.shape_round_outline_ripple
                else R.drawable.shape_round_outline_ripple_on_variant
            NotificationPresentation(
                id = notification.id,
                announcement = notification.announcement.copy(
                    date = dateFormatter(
                        notification.announcement.date,
                        "dd MMMM yyyy"
                    )
                ),
                background = backgroundDrawable
            )
        }
}