package gr.cpaleop.core.data.interceptors

import gr.cpaleop.core.domain.repositories.PreferencesRepository
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val preferencesRepository: PreferencesRepository) : Interceptor {

    companion object {
        private const val AUTH_HEADER_KEY = "x-access-token"
        private const val LANGUAGE_HEADER_KEY = "language"
        private const val AUTH_CONTENT_TYPE = "Content-Type"
        private const val AUTH_JSON_CONTENT_TYPE = "application/json; charset=UTF-8"
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        val token = preferencesRepository.getString(PreferencesRepository.ACCESS_TOKEN)
        val request = chain.request()

        if (token.isNullOrBlank()) {
            return chain.proceed(
                request.newBuilder()
                    .addHeader(AUTH_HEADER_KEY, "")
                    .build()
            )
        }

        val newRequest = request.newBuilder()
            .addHeader(AUTH_HEADER_KEY, token)
            .addHeader(AUTH_CONTENT_TYPE, AUTH_JSON_CONTENT_TYPE)
            .build()
        return chain.proceed(newRequest)
    }
}