package gr.cpaleop.profile.presentation.settings

import androidx.annotation.DrawableRes

data class Setting(
    val type: SettingType,
    @DrawableRes
    val iconRes: Int? = null,
    val title: String,
    val value: String? = null
)

enum class SettingType {
    SECTION_TITLE, CONTENT
}