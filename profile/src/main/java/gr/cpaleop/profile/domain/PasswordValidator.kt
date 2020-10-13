package gr.cpaleop.profile.domain

/**
 * Used to validate password. Rules can be added per implementation
 */
interface PasswordValidator {

    operator fun invoke(password: String): Boolean
}