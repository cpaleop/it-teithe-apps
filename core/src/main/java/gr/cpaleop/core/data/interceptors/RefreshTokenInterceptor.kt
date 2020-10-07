package gr.cpaleop.core.data.interceptors

import gr.cpaleop.core.domain.repositories.AuthenticationRepository
import gr.cpaleop.core.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class RefreshTokenInterceptor(
    private val preferencesRepository: PreferencesRepository,
    private val authenticationRepository: AuthenticationRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val originalResponse = chain.proceed(originalRequest)

        return runBlocking {
            if (isTokenExpired(originalResponse)) {
                originalResponse.close()
                refreshToken()
                return@runBlocking chain.proceed(originalRequest)
            } else {
                return@runBlocking originalResponse
            }
        }
    }

    /**
     * This is not enough to determine that token needs to be refreshed.
     * TODO: Deserialize response and determine by json payload code if token needs to be refreshed.
     */
    private fun isTokenExpired(response: Response): Boolean =
        response.code == UNAUTHORIZED_HTTP_ERROR

    private suspend fun refreshToken() {
        val newToken =
            authenticationRepository.refreshToken(
                preferencesRepository.getTokenFlow().first().refreshToken
            )
        preferencesRepository.updateToken(newToken)
    }

    companion object {

        private const val UNAUTHORIZED_HTTP_ERROR = 401
    }
}