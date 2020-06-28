package gr.cpaleop.core.presentation

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
            "dd-MM-yyyy, HH:mm",
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
}