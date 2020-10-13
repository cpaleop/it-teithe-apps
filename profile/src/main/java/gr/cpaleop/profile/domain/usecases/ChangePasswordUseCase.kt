package gr.cpaleop.profile.domain.usecases

interface ChangePasswordUseCase {

    suspend operator fun invoke(oldPassword: String, newPassword: String)
}