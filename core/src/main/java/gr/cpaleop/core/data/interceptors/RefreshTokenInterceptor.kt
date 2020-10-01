package gr.cpaleop.core.data.interceptors

import com.google.gson.Gson
import gr.cpaleop.core.domain.entities.Token
import gr.cpaleop.core.domain.repositories.AuthenticationRepository
import gr.cpaleop.core.domain.repositories.PreferencesRepository
import gr.cpaleop.core.domain.repositories.PreferencesRepository.Companion.ACCESS_TOKEN
import gr.cpaleop.core.domain.repositories.PreferencesRepository.Companion.REFRESH_TOKEN
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

@Suppress("BlockingMethodInNonBlockingContext")
class RefreshTokenInterceptor(
    private val preferencesRepository: PreferencesRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val gson: Gson
) : Interceptor {

    private var isRefreshing: Boolean = false

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val originalResponse = chain.proceed(originalRequest)

        return runBlocking {
            if (isTokenExpired(originalResponse)) {
                if (!isRefreshing) {
                    originalResponse.close()
                    refreshToken()
                    return@runBlocking chain.proceed(originalRequest)
                } else {
                    originalResponse.close()
                    return@runBlocking retryOriginalRequest(chain, originalRequest)
                }
            } else {
                return@runBlocking originalResponse
            }
        }
    }

    private fun isTokenExpired(response: Response): Boolean =
        response.code == UNAUTHORIZED_HTTP_ERROR

    private suspend fun refreshToken() {
        isRefreshing = true
        val newToken =
            authenticationRepository.refreshToken(
                preferencesRepository.getString(
                    REFRESH_TOKEN
                )
            )
        saveToken(newToken)
        isRefreshing = false
    }

    private suspend fun retryOriginalRequest(
        chain: Interceptor.Chain,
        originalRequest: Request
    ): Response {
        var retryCount = 1
        var response: Response
        do {
            delay(RETRY_DELAY_MS)
            response = chain.proceed(originalRequest)
            val tokenExpired = if (isTokenExpired(response)) {
                response.close()
                true
            } else {
                false
            }
        } while (++retryCount < RETRY_MAX_TRIES && tokenExpired)
        return response
    }

    private suspend fun saveToken(newToken: Token) {
        preferencesRepository.putString(ACCESS_TOKEN, newToken.accessToken)
        preferencesRepository.putString(REFRESH_TOKEN, newToken.refreshToken)
    }

    companion object {

        private const val UNAUTHORIZED_HTTP_ERROR = 401
        private const val RETRY_DELAY_MS: Long = 3000
        private const val RETRY_MAX_TRIES = 3
    }
}