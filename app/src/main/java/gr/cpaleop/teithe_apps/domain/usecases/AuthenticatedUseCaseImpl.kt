package gr.cpaleop.teithe_apps.domain.usecases

import gr.cpaleop.core.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.first

class AuthenticatedUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    AuthenticatedUseCase {

    override suspend fun invoke(): Boolean {
        val token = preferencesRepository.getTokenFlow().first()
        return token.accessToken.isNotEmpty() &&
                token.refreshToken.isNotEmpty()
    }
}