package gr.cpaleop.profile.presentation.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Setting(
    val type: SettingType,
    @DrawableRes
    val iconRes: Int? = null,
    @StringRes
    val titleRes: Int,
    val valueRes: Int? = null,
    val argument: String? = null
)

enum class SettingType {
    SECTION_TITLE, CONTENT
}