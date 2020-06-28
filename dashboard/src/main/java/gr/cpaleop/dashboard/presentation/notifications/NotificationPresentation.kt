package gr.cpaleop.dashboard.presentation.notifications

import androidx.annotation.DrawableRes
import gr.cpaleop.dashboard.domain.entities.NotificationRelatedAnnouncement

data class NotificationPresentation(
    val id: String,
    @DrawableRes
    val background: Int,
    val announcement: NotificationRelatedAnnouncement
)