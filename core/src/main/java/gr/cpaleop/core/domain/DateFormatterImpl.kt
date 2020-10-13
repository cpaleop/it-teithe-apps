package gr.cpaleop.core.domain

import java.text.SimpleDateFormat
import java.util.*


class DateFormatterImpl : DateFormatter {

    /**
     * Date transformation from epoch to human readable format
     *
     * @param time Time epoch in [Long]
     * @return Human readable date in [String]
     */
    override fun invoke(time: Long): String {
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = time * 1000
        return SimpleDateFormat(
            DateFormatter.ANNOUNCEMENT_DATE_FORMAT,
            Locale.getDefault()
        ).format(calendar.timeInMillis)
    }

    /**
     * Date transformation from epoch to human readable format
     *
     * @param time Time epoch in [Long]
     * @param format The readable format
     * @return Human readable date in [String]
     */
    override fun invoke(time: Long, format: String): String {
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = time * 1000
        return SimpleDateFormat(
            format,
            Locale.getDefault()
        ).format(calendar.timeInMillis)
    }

    /**
     * Date transformation from ISO 8601 to human readable format with a target format
     *
     * @param isoFormattedTimeStamp ISO 8601 formatted date in [String]
     * @param targetFormat The readable format of result
     * @return Human readable date in [String] formatted with [targetFormat]
     */
    override fun invoke(isoFormattedTimeStamp: String, targetFormat: String): String {
        val formatter = SimpleDateFormat(
            DateFormatter.FORMAT_ISO_8601,
            Locale.getDefault()
        )
        val date = formatter.parse(isoFormattedTimeStamp)
        val time = date?.time ?: 0L

        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = time/* * 1000*/
        return SimpleDateFormat(
            targetFormat,
            Locale.getDefault()
        ).format(calendar.timeInMillis)
    }

    override fun invoke(isoFormattedTimeStamp: String): Long {
        val formatter = SimpleDateFormat(
            DateFormatter.FORMAT_ISO_8601,
            Locale.getDefault()
        )
        val date = formatter.parse(isoFormattedTimeStamp)
        return date?.time ?: 0L
    }

    override fun fileFormat(time: Long, format: String): String {
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = time
        return SimpleDateFormat(
            format,
            Locale.getDefault()
        ).format(calendar.timeInMillis)
    }

    override fun getLocalTimestampFromUtc(time: String): Long {
        val sdf = SimpleDateFormat(DateFormatter.LDAP_DATE_FORMAT, Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        return (sdf.parse(time)?.time ?: 0L) / 1000
    }
}