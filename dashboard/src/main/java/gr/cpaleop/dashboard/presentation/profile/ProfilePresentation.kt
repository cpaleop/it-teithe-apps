package gr.cpaleop.dashboard.presentation.profile

import androidx.annotation.DrawableRes
import gr.cpaleop.dashboard.domain.entities.Social

data class ProfilePresentation(
    val profilePhotoUrl: String,
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
    val type: ProfileDetailsType,
    @DrawableRes
    val socialLogoResource: Int,
    val label: String,
    val content: String
)

enum class ProfileDetailsType {
    CONTENT, TITLE
}