package gr.cpaleop.profile.presentation.options

import androidx.annotation.DrawableRes

data class ProfileOption(
    val name: String,
    @DrawableRes
    val iconResource: Int
)