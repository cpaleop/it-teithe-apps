package gr.cpaleop.teithe_apps.data

import com.google.gson.Gson
import gr.cpaleop.core.data.model.response.RemoteAnnouncement
import gr.cpaleop.core.data.model.response.RemoteBuggyAnnouncement
import okhttp3.ResponseBody
import retrofit2.Converter

class RemoteAnnouncementConverter(
    private val remoteAnnouncementMapper: RemoteAnnouncementMapper,
    private val gson: Gson
) : Converter<ResponseBody, RemoteAnnouncement?> {

    override fun convert(value: ResponseBody): RemoteAnnouncement? {
        val body = value.string()
        val remoteBuggyAnnouncement = try {
            gson.fromJson(body, RemoteAnnouncement::class.java)
        } catch (t: Throwable) {
            gson.fromJson(body, RemoteBuggyAnnouncement::class.java)
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