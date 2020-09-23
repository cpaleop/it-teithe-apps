package gr.cpaleop.dashboard.presentation.documents.sort

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import gr.cpaleop.dashboard.domain.entities.DocumentSortType

data class DocumentSortOption(
    @DocumentSortType
    val type: Int,
    @StringRes
    val labelResource: Int,
    @DrawableRes
    val imageResource: Int,
    val selected: Boolean,
    val descending: Boolean
)