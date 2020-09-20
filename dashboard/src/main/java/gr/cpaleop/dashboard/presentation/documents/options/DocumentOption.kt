package gr.cpaleop.dashboard.presentation.documents.options

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import gr.cpaleop.dashboard.domain.entities.DocumentOptionType

data class DocumentOption(
    val type: DocumentOptionType,
    @StringRes
    val name: Int,
    @DrawableRes
    val iconResource: Int
)