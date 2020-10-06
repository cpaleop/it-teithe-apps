package gr.cpaleop.core.data.interceptors

import gr.cpaleop.core.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val preferencesRepository: PreferencesRepository) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { retrieveToken() }
        val request = chain.request()

        if (token.isBlank()) {
            return chain.proceed(request)
        }

        val newRequest = request.newBuilder()
            .addHeader(AUTH_HEADER_KEY, token)
            .addHeader(AUTH_CONTENT_TYPE, AUTH_JSON_CONTENT_TYPE)
            .build()
        return chain.proceed(newRequest)
    }

    private suspend fun retrieveToken(): String {
        return preferencesRepository.getTokenFlow().first().accessToken
    }

    companion object {
        private const val AUTH_HEADER_KEY = "x-access-token"
        private const val AUTH_CONTENT_TYPE = "Content-Type"
        private const val AUTH_JSON_CONTENT_TYPE = "application/json; charset=UTF-8"
    }
}