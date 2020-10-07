package gr.cpaleop.profile.presentation.settings

import androidx.annotation.StringRes
import gr.cpaleop.core.domain.behavior.LanguageCode
import gr.cpaleop.profile.R

class LanguageMapper {

    @StringRes
    operator fun invoke(@LanguageCode languageCode: String): Int {
        return when (languageCode) {
            LanguageCode.GREEK -> R.string.profile_language_greek
            LanguageCode.ENGLISH -> R.string.profile_language_english
            else -> throw IllegalArgumentException("No language code found with value $languageCode")
        }
    }
}