package gr.cpaleop.dashboard.domain.entities

data class Profile(
    val email: String,
    val personalDetails: ProfilePersonalDetails,
    val academicDetails: ProfileAcademicDetails,
    val socialMedia: SocialMedia
)

data class ProfileAcademicDetails(
    val am: String,
    val type: String,
    val username: String,
    val displayName: String,
    val registeredYear: String,
    val currentSemester: String
)

data class ProfilePersonalDetails(
    val lastName: String,
    val givenName: String,
    val websiteUrl: String,
    val description: String,
    val profileImageUrl: String,
    val telephoneNumber: String
)

data class SocialMedia(
    val facebook: String,
    val twitter: String,
    val github: String,
    val googlePlus: String,
    val linkedIn: String
)