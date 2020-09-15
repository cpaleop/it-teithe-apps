package gr.cpaleop.authentication.data

import gr.cpaleop.authentication.domain.repositories.PreferencesRepository
import gr.cpaleop.core.domain.entities.Token
import gr.cpaleop.core.domain.repositories.PreferencesRepository.Companion.ACCESS_TOKEN
import gr.cpaleop.core.domain.repositories.PreferencesRepository.Companion.REFRESH_TOKEN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PreferencesRepositoryImpl(private val preferencesRepository: gr.cpaleop.core.domain.repositories.PreferencesRepository) :
    PreferencesRepository {

    override suspend fun saveToken(token: Token) = withContext(Dispatchers.IO) {
        preferencesRepository.putStringAsync(ACCESS_TOKEN, token.accessToken)
        preferencesRepository.putStringAsync(REFRESH_TOKEN, token.refreshToken)
    }
}