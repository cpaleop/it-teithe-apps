package gr.cpaleop.profile.presentation.options

import androidx.annotation.StringRes
import gr.cpaleop.profile.domain.entities.Social

class SelectedSocialOption(
    val social: Social,
    @StringRes
    val titleRes: Int,
    val value: String
)