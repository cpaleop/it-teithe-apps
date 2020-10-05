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

    fun clear()

    companion object {

        const val PREFERENCES_FILE_KEY = "gr.cpaleop.itteitheapps.preferences"

        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
        const val NIGHT_MODE = "NIGHT_MODE"
        const val LANGUAGE = "LANGUAGE_CODE"
        const val DOCUMENT_SORT_TYPE = "DOCUMENT_SORT_TYPE"
        const val DOCUMENT_SORT_DESCENDING = "DOCUMENT_SORT_DESCENDING"
        const val DOCUMENT_PREVIEW = "DOCUMENT_PREVIEW"
    }
}