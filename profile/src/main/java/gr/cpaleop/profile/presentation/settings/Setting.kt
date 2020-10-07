package gr.cpaleop.profile.presentation.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Setting(
    val type: SettingType,
    @DrawableRes
    val iconRes: Int? = null,
    @StringRes
    val titleRes: Int,
    @StringRes
    val valueRes: Int? = null
)

enum class SettingType {
    SECTION_TITLE, CONTENT
}