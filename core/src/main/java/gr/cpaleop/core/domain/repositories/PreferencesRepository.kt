package gr.cpaleop.core.domain.repositories

interface PreferencesRepository {

    fun putString(key: String?, value: String?)

    fun getString(key: String?): String?

    fun putBoolean(key: String?, value: Boolean)

    fun getBoolean(key: String?): Boolean

    fun putInt(key: String?, value: Int)

    fun getInt(key: String?): Int

    companion object {

        const val PREFERENCES_FILE_KEY = "gr.cpaleop.itteitheapps.preferences"

        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
        const val NIGHT_MODE = "NIGHT_MODE"
        const val LANGUAGE = "LANGUAGE_CODE"
    }
}