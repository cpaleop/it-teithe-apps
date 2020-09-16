package gr.cpaleop.dashboard.presentation.profile

data class ProfilePresentation(
    val profilePhotoUrl: String,
    val am: String,
    val email: String,
    val username: String,
    val displayName: String,
    val semester: String,
    val registeredYear: String,
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