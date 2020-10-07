package gr.cpaleop.profile.presentation.settings.theme

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import gr.cpaleop.profile.R

class ThemeMapper {

    @StringRes
    operator fun invoke(theme: Int): Int {
        return when (theme) {
            AppCompatDelegate.MODE_NIGHT_YES -> R.string.profile_theme_dark
            AppCompatDelegate.MODE_NIGHT_NO -> R.string.profile_theme_light
            AppCompatDelegate.MODE_NIGHT_UNSPECIFIED,
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> R.string.profile_theme_system
            else -> throw IllegalArgumentException("No theme found with value: $theme")
        }
    }
}