package gr.cpaleop.core.data.remote

import gr.cpaleop.core.data.model.response.RemoteCategory
import retrofit2.http.GET

interface CategoriesApi {

    @GET("categories")
    suspend fun getCategories(): List<RemoteCategory>
}