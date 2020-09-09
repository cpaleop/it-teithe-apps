package gr.cpaleop.teithe_apps.domain.usecases

import gr.cpaleop.teithe_apps.domain.repositories.PreferencesRepository

class AuthenticatedUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    AuthenticatedUseCase {

    override suspend fun invoke(): Boolean {
        return !preferencesRepository.getAccessToken().isNullOrEmpty()
                && !preferencesRepository.getRefreshToken().isNullOrEmpty()
    }
}