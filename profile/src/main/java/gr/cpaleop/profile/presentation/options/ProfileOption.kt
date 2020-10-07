package gr.cpaleop.profile.presentation.options

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ProfileOption(
    @StringRes
    val labelRes: Int,
    @DrawableRes
    val iconResource: Int
)