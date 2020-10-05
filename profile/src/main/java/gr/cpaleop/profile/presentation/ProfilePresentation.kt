package gr.cpaleop.profile.presentation

import androidx.annotation.DrawableRes
import gr.cpaleop.profile.domain.entities.Personal
import gr.cpaleop.profile.domain.entities.Social

data class ProfilePresentation(
    val profilePhotoUrl: String?,
    val am: String,
    val username: String,
    val displayName: String,
    val semester: String,
    val registeredYear: String,
    val social: List<ProfileSocialDetails>,
    val personalDetails: List<ProfilePersonalDetails>
)

data class ProfilePersonalDetails(
    val type: Personal,
    val label: String,
    val value: String
)

data class ProfileSocialDetails(
    val socialType: Social,
    @DrawableRes
    val socialLogoResource: Int,
    val label: String,
    val value: String
)