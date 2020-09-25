package gr.cpaleop.core.data.remote

import gr.cpaleop.core.data.model.response.RemoteAnnouncement
import gr.cpaleop.core.data.model.response.RemoteAnnouncementTitle
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AnnouncementsApi {

    @GET("announcements")
    suspend fun fetchAnnouncements(
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int
    ): List<RemoteAnnouncement>

    @GET("announcements")
    suspend fun fetchAnnouncementsFiltered(
        @Query("q") filter: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int
    ): List<RemoteAnnouncement>

    @GET("announcements/{announcementId}")
    suspend fun fetchAnnouncementById(@Path("announcementId") announcementId: String): RemoteAnnouncement

    @GET("announcements/{announcementId}")
    suspend fun fetchAnnouncementTitleById(
        @Path("announcementId") announcementId: String,
        @Query("fields") query: String = "title,titleEn"
    ): List<RemoteAnnouncementTitle>

    @GET("announcements/")
    suspend fun fetchAnnouncementsByCategory(@Query("q") query: String): List<RemoteAnnouncement>
}