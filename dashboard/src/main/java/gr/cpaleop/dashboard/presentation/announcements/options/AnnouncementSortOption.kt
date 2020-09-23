package gr.cpaleop.dashboard.presentation.announcements.options

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import gr.cpaleop.dashboard.domain.entities.AnnouncementSortType

data class AnnouncementSortOption(
    @AnnouncementSortType
    val type: Int,
    @StringRes
    val labelResource: Int,
    @DrawableRes
    val imageResource: Int,
    val selected: Boolean,
    val descending: Boolean
)