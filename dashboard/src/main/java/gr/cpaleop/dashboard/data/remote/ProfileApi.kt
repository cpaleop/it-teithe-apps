package gr.cpaleop.dashboard.data.remote

import gr.cpaleop.dashboard.data.model.remote.RemoteProfile
import retrofit2.http.GET

interface ProfileApi {

    @GET("profile")
    suspend fun fetchProfile(): RemoteProfile
}