package gr.cpaleop.teithe_apps.domain.repositories

interface PreferencesRepository {

    suspend fun putString(key: String, value: String)

    suspend fun getAccessToken(): String?

    suspend fun getRefreshToken(): String?
}