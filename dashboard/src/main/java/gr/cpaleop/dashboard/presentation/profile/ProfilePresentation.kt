package gr.cpaleop.dashboard.presentation.profile

import androidx.annotation.DrawableRes

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
    val type: ProfileDetailsType,
    @DrawableRes
    val socialLogoResource: Int,
    val label: String,
    val content: String
)

enum class ProfileDetailsType {
    CONTENT, TITLE
}