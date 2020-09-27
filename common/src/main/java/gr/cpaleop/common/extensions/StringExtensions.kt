package gr.cpaleop.common.extensions

fun String?.orEmpty(fallbackValue: String = ""): String {
    if (this.isNullOrEmpty()) return fallbackValue
    else return this
}