package gr.cpaleop.authentication.data

import gr.cpaleop.authentication.domain.repositories.PreferencesRepository
import gr.cpaleop.core.domain.entities.Token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PreferencesRepositoryImpl(private val preferencesRepository: gr.cpaleop.core.domain.repositories.PreferencesRepository) :
    PreferencesRepository {

    override suspend fun saveToken(token: Token) = withContext(Dispatchers.IO) {
        preferencesRepository.updateToken(token)
    }
}