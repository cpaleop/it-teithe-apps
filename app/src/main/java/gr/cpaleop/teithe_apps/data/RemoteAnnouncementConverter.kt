package gr.cpaleop.teithe_apps.data

import gr.cpaleop.core.datasource.model.response.RemoteAnnouncement
import gr.cpaleop.core.datasource.model.response.RemoteBuggyAnnouncement
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import retrofit2.Converter

class RemoteAnnouncementConverter(
    private val json: Json,
    private val remoteAnnouncementMapper: RemoteAnnouncementMapper
) : Converter<ResponseBody, RemoteAnnouncement?> {

    override fun convert(value: ResponseBody): RemoteAnnouncement? {
        val body = value.string()
        val remoteBuggyAnnouncement = try {
            json.decodeFromString<RemoteAnnouncement>(body)
        } catch (t: Throwable) {
            json.decodeFromString<RemoteBuggyAnnouncement>(body)
        }
        return when (remoteBuggyAnnouncement) {
            is RemoteAnnouncement -> remoteBuggyAnnouncement
            is RemoteBuggyAnnouncement -> remoteAnnouncementMapper(
                remoteBuggyAnnouncement
            )
            else -> null
        }
    }
}