package gr.cpaleop.core.data.interceptors

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
import timber.log.Timber

@Suppress("BlockingMethodInNonBlockingContext")
class RefreshTokenInterceptor(
    private val preferencesRepository: PreferencesRepository,
    private val authenticationRepository: AuthenticationRepository
) : Interceptor {

    private var isRefreshing: Boolean = false

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val originalResponse = chain.proceed(originalRequest)

        return runBlocking {
            if (isTokenExpired(originalResponse)) {
                Timber.e("SKATA Token expired")
                if (!isRefreshing) {
                    Timber.e("SKATA Token expired and not refreshing. Time to refresh")
                    refreshToken()
                    return@runBlocking chain.proceed(originalRequest)
                } else {
                    return@runBlocking repeatOriginalRequest(chain, originalRequest)
                }
            } else {
                return@runBlocking originalResponse
            }
        }
    }

    private fun isTokenExpired(response: Response): Boolean =
        response.code == UNAUTHORIZED_HTTP_ERROR

    private suspend fun refreshToken() {
        Timber.e("SKATA Refreshing token")
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

    private suspend fun repeatOriginalRequest(
        chain: Interceptor.Chain,
        originalRequest: Request
    ): Response {
        var retryCount = 1
        var response: Response
        do {
            Timber.e("SKATA Retrying original request")
            delay(RETRY_DELAY_MS)
            response = chain.proceed(originalRequest)
        } while (++retryCount < RETRY_MAX_TRIES && isTokenExpired(response))
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