package gr.cpaleop.profile.presentation

import androidx.annotation.DrawableRes
import gr.cpaleop.profile.domain.entities.Social

data class ProfilePresentation(
    val profilePhotoUrl: String?,
    val am: String,
    val email: String,
    val username: String,
    val displayName: String,
    val semester: String,
    val registeredYear: String,
    val social: List<ProfileSocialDetails>
)

data class ProfileSocialDetails(
    val socialType: Social,
    @DrawableRes
    val socialLogoResource: Int,
    val label: String,
    val content: String
)