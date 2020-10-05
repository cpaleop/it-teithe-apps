package gr.cpaleop.profile.domain.usecases

import gr.cpaleop.profile.domain.repositories.PreferencesRepository

class LogoutUseCaseImpl(private val preferencesRepository: PreferencesRepository) : LogoutUseCase {

    override suspend fun invoke() {
        preferencesRepository.clearUserData()
    }
}