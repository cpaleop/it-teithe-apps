package gr.cpaleop.dashboard.presentation.profile

data class ProfilePresentation(
    val profilePhotoUrl: String,
    val personalDetails: List<ProfilePresentationDetails>,
    val academicDetails: List<ProfilePresentationDetails>,
    val social: List<ProfilePresentationDetails>
)

data class ProfilePresentationDetails(
    val type: ProfileDetailsType,
    val title: String,
    val content: String
)

enum class ProfileDetailsType {
    CONTENT, TITLE
}