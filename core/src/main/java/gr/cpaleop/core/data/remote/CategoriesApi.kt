package gr.cpaleop.core.data.remote

import gr.cpaleop.core.data.model.response.RemoteCategory
import gr.cpaleop.core.data.model.response.RemoteRegisteredCategory
import retrofit2.http.GET

interface CategoriesApi {

    @GET("categories")
    suspend fun fetchCategories(): List<RemoteCategory>

    @GET("categories/isRegistered")
    suspend fun fetchRegisteredCategories(): List<RemoteRegisteredCategory>
}