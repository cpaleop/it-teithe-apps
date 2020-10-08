package gr.cpaleop.core.presentation

import androidx.annotation.StringRes

data class Message(
    @StringRes
    val resource: Int,
    var arguments: List<Any> = emptyList()
)