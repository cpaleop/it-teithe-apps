package gr.cpaleop.dashboard.presentation.files.sort

import androidx.annotation.StringRes
import gr.cpaleop.dashboard.domain.entities.DocumentSortType

data class DocumentSortOption(
    @DocumentSortType
    val type: Int,
    @StringRes
    val label: Int,
    val selected: Boolean,
    val descending: Boolean
)