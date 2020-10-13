package gr.cpaleop.profile.domain.usecases

import gr.cpaleop.profile.domain.PasswordValidator
import gr.cpaleop.profile.domain.entities.InvalidPasswordException
import gr.cpaleop.profile.domain.repositories.ProfileRepository

class ChangePasswordUseCaseImpl(
    private val passwordValidator: PasswordValidator,
    private val profileRepository: ProfileRepository
) : ChangePasswordUseCase {

    override suspend fun invoke(oldPassword: String, newPassword: String) {
        if (!passwordValidator(newPassword)) throw InvalidPasswordException()
        return profileRepository.changePassword(oldPassword, newPassword)
    }
}