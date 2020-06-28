package gr.cpaleop.dashboard.presentation.notifications

import gr.cpaleop.dashboard.domain.entities.Notification
import gr.cpaleop.teithe_apps.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotificationPresentationMapper {

    suspend operator fun invoke(notification: Notification): NotificationPresentation =
        withContext(Dispatchers.Default) {
            val backgroundDrawable =
                if (notification.seen) R.drawable.shape_round_outline_ripple
                else R.drawable.shape_round_outline_ripple_on_variant
            NotificationPresentation(
                id = notification.id,
                announcement = notification.announcement,
                background = backgroundDrawable
            )
        }
}