package gr.cpaleop.documents.presentation.options

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import gr.cpaleop.documents.domain.entities.DocumentOptionType

data class DocumentOption(
    val type: DocumentOptionType,
    @StringRes
    val name: Int,
    @DrawableRes
    val iconResource: Int
)