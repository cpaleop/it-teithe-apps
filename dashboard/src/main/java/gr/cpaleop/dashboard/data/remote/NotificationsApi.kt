package gr.cpaleop.dashboard.data.remote

import gr.cpaleop.dashboard.data.model.remote.NotificationsResponse
import retrofit2.http.GET

interface NotificationsApi {

    @GET("notifications")
    suspend fun getNotifications(): NotificationsResponse
}