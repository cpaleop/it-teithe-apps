package gr.cpaleop.profile.domain.entities

data class Profile(
    val personalDetails: PersonalDetails,
    val academicDetails: AcademicDetails,
    val socialMedia: SocialMedia,
    val passwordLastChangedTime: Long
)

data class AcademicDetails(
    val am: String,
    val type: String,
    val username: String,
    val displayName: String,
    val registeredYear: String,
    val currentSemester: String
)

data class PersonalDetails(
    val email: String,
    val lastName: String,
    val givenName: String,
    val websiteUrl: String,
    val description: String,
    val profileImageUrl: String?,
    val telephoneNumber: String
)

data class SocialMedia(
    val facebook: String,
    val twitter: String,
    val github: String,
    val googlePlus: String,
    val linkedIn: String
)