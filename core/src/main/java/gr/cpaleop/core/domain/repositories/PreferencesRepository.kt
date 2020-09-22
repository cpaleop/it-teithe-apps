package gr.cpaleop.core.domain.repositories

interface PreferencesRepository {

    fun putStringAsync(key: String?, value: String?)

    suspend fun putString(key: String?, value: String?)

    fun getString(key: String?): String?

    fun putBooleanAsync(key: String?, value: Boolean)

    suspend fun putBoolean(key: String?, value: Boolean)

    fun getBoolean(key: String?): Boolean

    fun putIntAsync(key: String?, value: Int)

    suspend fun putInt(key: String?, value: Int)

    fun getInt(key: String?): Int

    companion object {

        const val PREFERENCES_FILE_KEY = "gr.cpaleop.itteitheapps.preferences"

        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
        const val NIGHT_MODE = "NIGHT_MODE"
        const val LANGUAGE = "LANGUAGE_CODE"
        const val ANNOUNCEMENT_SORT_TYPE = "ANNOUNCEMENT_SORT_TYPE"
        const val ANNOUNCEMENT_SORT_DESCENDING = "ANNOUNCEMENT_SORT_DESCENDING"
        const val FILE_SORT_TYPE = "FILE_SORT_TYPE"
        const val FILE_SORT_DESCENDING = "FILE_SORT_DESCENDING"
    }
}