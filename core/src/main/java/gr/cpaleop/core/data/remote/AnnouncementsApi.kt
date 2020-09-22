package gr.cpaleop.core.data.remote

import gr.cpaleop.core.data.model.response.RemoteAnnouncement
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AnnouncementsApi {

    @GET("announcements")
    suspend fun fetchAnnouncements(
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
        @Query("sort") sort: String
    ): List<RemoteAnnouncement>

    @GET("announcements")
    suspend fun fetchAnnouncementsFiltered(
        @Query("q") filter: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
        @Query("sort") sort: String
    ): List<RemoteAnnouncement>

    @GET("announcements/{announcementId}")
    suspend fun fetchAnnouncementById(@Path("announcementId") announcementId: String): RemoteAnnouncement

    @GET("announcements/")
    suspend fun fetchAnnouncementsByCategory(@Query("q") query: String): List<RemoteAnnouncement>
}