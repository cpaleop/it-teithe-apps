package gr.cpaleop.core.presentation

import androidx.annotation.StringRes

class SnackbarMessage(
    @StringRes
    val resource: Int,
    vararg var arguments: Any
)