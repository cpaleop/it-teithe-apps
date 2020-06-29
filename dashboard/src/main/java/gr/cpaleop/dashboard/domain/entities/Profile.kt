package gr.cpaleop.dashboard.domain.entities

data class Profile(
    val username: String,
    val am: String,
    val type: String,
    val givenName: String,
    val lastName: String,
    val websiteUrl: String,
    val displayName: String,
    val description: String,
    val registeredYear: String,
    val currentSemester: String,
    val telephoneNumber: String,
    val profileImageUrl: String,
    val socialMedia: SocialMedia
)

data class SocialMedia(
    val facebook: String,
    val twitter: String,
    val github: String,
    val googlePlus: String,
    val linkedIn: String?
)