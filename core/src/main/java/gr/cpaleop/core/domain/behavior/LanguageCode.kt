package gr.cpaleop.core.domain.behavior

import androidx.annotation.StringDef

@StringDef(
    LanguageCode.GREEK,
    LanguageCode.ENGLISH
)
annotation class LanguageCode {

    companion object {

        const val GREEK = "el"
        const val ENGLISH = "en"
    }
}