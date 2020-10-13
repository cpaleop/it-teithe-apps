package gr.cpaleop.profile.domain

class PasswordValidatorImpl : PasswordValidator {

    override fun invoke(password: String): Boolean = password.isNotEmpty() && !password.isBlank()
}