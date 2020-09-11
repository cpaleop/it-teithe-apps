package gr.cpaleop.core.data.remote

import gr.cpaleop.core.data.model.response.RemoteCategory
import gr.cpaleop.core.data.model.response.RemoteRegisteredCategory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PUT

interface CategoriesApi {

    @GET("categories")
    suspend fun fetchCategories(): List<RemoteCategory>

    @GET("categories/isRegistered")
    suspend fun fetchRegisteredCategories(): List<RemoteRegisteredCategory>

    @FormUrlEncoded
    @PUT("categories/register")
    suspend fun updateRegisteredCategories(
        @Field("addCat") registeredCategories: String,
        @Field("removeCat") nonRegisteredCategories: String
    )
}