package gr.cpaleop.core.data.remote

import gr.cpaleop.core.data.model.response.RemoteAnnouncement
import gr.cpaleop.core.data.model.response.RemoteAnnouncementTitle
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface AnnouncementsApi {

    @GET("announcements/public")
    suspend fun fetchPublicAnnouncements(): List<RemoteAnnouncement>

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

    @POST("announcements/")
    @Multipart
    suspend fun createAnnouncement(
        @Part files: MultipartBody.Part?,
        @Part("title") title: RequestBody,
        @Part("titleEn") titleEn: RequestBody,
        @Part("text") text: RequestBody,
        @Part("textEn") textEn: RequestBody,
        @Part("about") about: RequestBody
    )
}