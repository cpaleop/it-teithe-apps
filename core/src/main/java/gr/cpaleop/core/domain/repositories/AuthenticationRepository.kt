package gr.cpaleop.core.domain.repositories

import gr.cpaleop.core.domain.entities.Token

interface AuthenticationRepository {

    suspend fun getToken(code: String?): Token

    suspend fun refreshToken(refreshToken: String?): Token
}