package gr.cpaleop.core.domain

interface DateFormatter {

    operator fun invoke(time: Long): String

    operator fun invoke(time: Long, format: String): String

    operator fun invoke(isoFormattedTimeStamp: String): Long

    operator fun invoke(isoFormattedTimeStamp: String, targetFormat: String): String

    fun fileFormat(time: Long, format: String): String

    companion object {

        const val ANNOUNCEMENT_DATE_FORMAT = "dd-MM-yyyy, HH:mm"
    }
}