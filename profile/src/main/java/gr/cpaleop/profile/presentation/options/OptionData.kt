package gr.cpaleop.profile.presentation.options

import androidx.annotation.StringRes

data class OptionData(
    @StringRes
    val titleRes: Int,
    val value: String
)