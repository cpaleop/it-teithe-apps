package gr.cpaleop.dashboard.presentation.profile.options

import androidx.annotation.DrawableRes

data class ProfileOption(
    val name: String,
    @DrawableRes
    val iconResource: Int
)