package gr.cpaleop.core.data.interceptors

import gr.cpaleop.core.domain.entities.Token
import gr.cpaleop.core.domain.repositories.AuthenticationRepository
import gr.cpaleop.core.domain.repositories.PreferencesRepository
import gr.cpaleop.core.domain.repositories.PreferencesRepository.Companion.ACCESS_TOKEN
import gr.cpaleop.core.domain.repositories.PreferencesRepository.Companion.REFRESH_TOKEN
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class RefreshTokenInterceptor(
    private val preferencesRepository: PreferencesRepository,
    private val authenticationRepository: AuthenticationRepository
) : Interceptor {

    @Suppress("BlockingMethodInNonBlockingContext")
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val originalResponse = chain.proceed(originalRequest)

        if (isTokenExpired(originalResponse)) {
            Timber.e("SKATA TOKEN EXPIRED")
            return runBlocking {
                val newToken =
                    authenticationRepository.refreshToken(
                        preferencesRepository.getString(
                            REFRESH_TOKEN
                        )
                    )
                saveToken(newToken)

                Timber.e("SKATA RETRY REQUEST WITH NEW TOKEN ${newToken.accessToken}")
                return@runBlocking chain.proceed(originalRequest)
            }
        } else {
            return originalResponse
        }
    }

    private fun isTokenExpired(response: Response): Boolean {
        return response.code == 401 || response.code == 4001
    }

    private fun saveToken(newToken: Token) {
        Timber.e("SKATA NEW ACCESS TOKEN ${newToken.accessToken}")
        Timber.e("SKATA NEW ACCESS TOKEN ${newToken.refreshToken}")

        preferencesRepository.putString(ACCESS_TOKEN, newToken.accessToken)
        preferencesRepository.putString(REFRESH_TOKEN, newToken.refreshToken)
    }
}