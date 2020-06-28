package gr.cpaleop.core.domain.entities

data class Token(
    val accessToken: String,
    val refreshToken: String
)