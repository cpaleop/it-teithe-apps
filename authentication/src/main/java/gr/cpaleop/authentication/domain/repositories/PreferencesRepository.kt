package gr.cpaleop.authentication.domain.repositories

import gr.cpaleop.core.domain.entities.Token

interface PreferencesRepository {

    suspend fun saveToken(token: Token)
}