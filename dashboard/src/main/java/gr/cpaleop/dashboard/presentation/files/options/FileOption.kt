package gr.cpaleop.dashboard.presentation.files.options

import androidx.annotation.DrawableRes

data class FileOption(
    val name: String,
    @DrawableRes
    val imageResource: Int
)