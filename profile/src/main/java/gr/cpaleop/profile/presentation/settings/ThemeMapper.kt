package gr.cpaleop.profile.presentation.settings

import androidx.appcompat.app.AppCompatDelegate

class ThemeMapper {

    operator fun invoke(theme: Int): String {
        return when (theme) {
            AppCompatDelegate.MODE_NIGHT_YES -> "Dark"
            AppCompatDelegate.MODE_NIGHT_NO -> "Light"
            AppCompatDelegate.MODE_NIGHT_UNSPECIFIED,
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> "Follow system"
            else -> throw IllegalArgumentException("No theme found with value: $theme")
        }
    }
}