package gr.cpaleop.dashboard.data.remote

import gr.cpaleop.dashboard.data.model.remote.NotificationsResponse
import retrofit2.http.GET
import retrofit2.http.POST

interface NotificationsApi {

    @GET("notifications")
    suspend fun fetchNotifications(): NotificationsResponse

    @POST("notifications")
    suspend fun readAllNotifications()
}