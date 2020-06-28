package gr.cpaleop.core.data

import gr.cpaleop.core.BuildConfig
import gr.cpaleop.core.data.mappers.TokenMapper
import gr.cpaleop.core.data.remote.AuthenticationApi
import gr.cpaleop.core.domain.entities.Token
import gr.cpaleop.core.domain.repositories.AuthenticationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthenticationRepositoryImpl(
    private val authenticationApi: AuthenticationApi,
    private val tokenMapper: TokenMapper
) : AuthenticationRepository {

    override suspend fun getToken(code: String?): Token = withContext(Dispatchers.IO) {
        val response = authenticationApi.getToken(
            code = code,
            clientId = BuildConfig.CLIENT_ID,
            clientSecret = BuildConfig.CLIENT_SECRET,
            grantType = AUTHORIZATION_CODE_GRANT_TYPE
        )
        return@withContext tokenMapper(response)
    }

    override suspend fun refreshToken(refreshToken: String?): Token = withContext(Dispatchers.IO) {
        val response = authenticationApi.getToken(
            code = refreshToken,
            clientId = BuildConfig.CLIENT_ID,
            clientSecret = BuildConfig.CLIENT_SECRET,
            grantType = REFRESH_TOKEN_GRANT_TYPE
        )
        return@withContext tokenMapper(response)
    }

    companion object {

        private const val AUTHORIZATION_CODE_GRANT_TYPE = "authorization_code"
        private const val REFRESH_TOKEN_GRANT_TYPE = "refresh_token"
    }
}