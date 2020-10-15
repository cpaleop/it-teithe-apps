package gr.cpaleop.core.presentation

import androidx.annotation.StringRes

class Message(
    @StringRes
    val resource: Int,
    vararg val arguments: Any?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Message

        if (resource != other.resource) return false
        if (!arguments.contentEquals(other.arguments)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = resource
        result = 31 * result + arguments.contentHashCode()
        return result
    }
}