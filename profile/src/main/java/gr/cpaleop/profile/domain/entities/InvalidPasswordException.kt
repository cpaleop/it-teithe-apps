package gr.cpaleop.profile.domain.entities

data class InvalidPasswordException(override val message: String? = "Invalid password") :
    Throwable()