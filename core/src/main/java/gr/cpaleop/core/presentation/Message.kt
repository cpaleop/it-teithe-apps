package gr.cpaleop.core.presentation

import androidx.annotation.StringRes

class Message(
    @StringRes
    val resource: Int,
    vararg var arguments: Any
)