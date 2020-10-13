package gr.cpaleop.core.domain

interface DateFormatter {

    operator fun invoke(time: Long): String

    operator fun invoke(time: Long, format: String): String

    operator fun invoke(isoFormattedTimeStamp: String): Long

    operator fun invoke(isoFormattedTimeStamp: String, targetFormat: String): String

    fun fileFormat(time: Long, format: String): String

    fun getLocalTimestampFromUtc(time: String): Long

    companion object {

        const val ANNOUNCEMENT_DATE_FORMAT = "dd-MM-yyyy, HH:mm"
        const val LDAP_DATE_FORMAT = "yyyyMMddHHmmss"
        const val LDAP_DATE_READABLE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy"
        const val FORMAT_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    }
}