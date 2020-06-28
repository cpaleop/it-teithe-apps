package gr.cpaleop.core.presentation

interface DateFormatter {

    operator fun invoke(time: Long): String

    operator fun invoke(time: Long, format: String): String

    operator fun invoke(isoFormattedTimeStamp: String, targetFormat: String): String
}