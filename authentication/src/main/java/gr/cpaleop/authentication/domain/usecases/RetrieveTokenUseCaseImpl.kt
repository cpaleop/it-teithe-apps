package gr.cpaleop.authentication.domain.usecases

import gr.cpaleop.authentication.domain.repositories.PreferencesRepository
import gr.cpaleop.core.domain.repositories.AuthenticationRepository

class RetrieveTokenUseCaseImpl(
    private val preferencesRepository: PreferencesRepository,
    private val authenticationRepository: AuthenticationRepository
) : RetrieveTokenUseCase {

    override suspend fun invoke(code: String?) {
        val token = authenticationRepository.getToken(code)
        preferencesRepository.saveToken(token)
    }
}