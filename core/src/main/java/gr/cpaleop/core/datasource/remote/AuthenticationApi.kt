package gr.cpaleop.core.datasource.remote

import gr.cpaleop.core.datasource.model.response.RemoteToken
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthenticationApi {

    @POST("token")
    @FormUrlEncoded
    suspend fun getToken(
        @Field("code") code: String?,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String
    ): RemoteToken

    @POST("token")
    @FormUrlEncoded
    suspend fun refreshToken(
        @Field("code") code: String?,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String
    ): RemoteToken
}