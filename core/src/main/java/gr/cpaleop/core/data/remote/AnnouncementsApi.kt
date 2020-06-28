package gr.cpaleop.core.data.remote

import gr.cpaleop.core.data.model.response.RemoteAnnouncement
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AnnouncementsApi {

    @GET("announcements")
    suspend fun getAnnouncements(
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int
    ): List<RemoteAnnouncement>

    @GET("announcements/{announcementId}")
    suspend fun getAnnouncementById(@Path("announcementId") announcementId: String): RemoteAnnouncement
}