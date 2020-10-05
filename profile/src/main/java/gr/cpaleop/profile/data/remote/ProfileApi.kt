package gr.cpaleop.profile.data.remote

import gr.cpaleop.profile.data.model.remote.RemoteProfile
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH

interface ProfileApi {

    @GET("profile")
    suspend fun fetchProfile(): RemoteProfile

    @PATCH("profile")
    @FormUrlEncoded
    suspend fun updateProfile(
        @Field("facebook") facebook: String? = null,
        @Field("twitter") twitter: String? = null,
        @Field("github") github: String? = null,
        @Field("googlePlus") googlePlus: String? = null,
        @Field("linkedIn") linkedIn: String? = null,
        @Field("description") description: String? = null,
        @Field("telephoneNumber") telephoneNumber: String? = null,
        @Field("mail") mail: String? = null,
        @Field("displayName;lang-el") displayName: String? = null
    )
}