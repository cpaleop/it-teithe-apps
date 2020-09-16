package gr.cpaleop.dashboard.presentation.announcements.options.sort

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class SortOption(
    @StringRes
    val nameResource: Int,
    val type: Type,
    val selected: Boolean
)

data class Type(
    val sortType: SortType,
    @DrawableRes
    val imageRes: Int
)