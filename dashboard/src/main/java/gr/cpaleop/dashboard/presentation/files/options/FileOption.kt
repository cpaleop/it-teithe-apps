package gr.cpaleop.dashboard.presentation.files.options

import androidx.annotation.DrawableRes

data class FileOption(
    val type: FileOptionType,
    val name: String,
    @DrawableRes
    val iconResource: Int
)

enum class FileOptionType {
    FILE, ANNOUNCEMENT
}