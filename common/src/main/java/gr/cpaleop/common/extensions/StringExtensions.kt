package gr.cpaleop.common.extensions

fun String?.safe(): String {
    return this ?: ""
}