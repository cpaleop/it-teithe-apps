package gr.cpaleop.profile.presentation.personal

import androidx.annotation.StringRes
import gr.cpaleop.profile.domain.entities.Personal

data class PersonalOptionData(
    val type: Personal,
    @StringRes
    val labelRes: Int,
    val value: String
)