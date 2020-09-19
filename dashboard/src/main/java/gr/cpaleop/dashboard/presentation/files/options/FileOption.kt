package gr.cpaleop.dashboard.presentation.files.options

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import gr.cpaleop.dashboard.domain.entities.FileOptionType

data class FileOption(
    val type: FileOptionType,
    @StringRes
    val name: Int,
    @DrawableRes
    val iconResource: Int
)