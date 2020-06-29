package gr.cpaleop.common.extensions

fun String?.safe(fallbackValue: String = ""): String {
    if (this.isNullOrEmpty()) return fallbackValue
    else return this
}