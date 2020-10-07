package gr.cpaleop.profile.presentation.settings

import android.content.Context
import gr.cpaleop.core.domain.behavior.LanguageCode
import gr.cpaleop.profile.R

class LanguageMapper(private val applicationContext: Context) {

    operator fun invoke(@LanguageCode languageCode: String): String {
        return when (languageCode) {
            LanguageCode.GREEK -> applicationContext.getString(R.string.profile_settings_language_greek)
            LanguageCode.ENGLISH -> applicationContext.getString(R.string.profile_settings_language_english)
            else -> throw IllegalArgumentException("No language code found with value $languageCode")
        }
    }
}