package gr.cpaleop.core.data.remote

import gr.cpaleop.core.data.model.response.RemoteCategory
import gr.cpaleop.core.data.model.response.RemoteRegisteredCategory
import retrofit2.http.*

interface CategoriesApi {

    @GET("categories/public")
    suspend fun fetchPublicCategories(): List<RemoteCategory>

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

    @GET("categories")
    suspend fun fetchCategoryById(@Query("q") categoryQuery: String): List<RemoteCategory>
}