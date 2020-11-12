package gr.cpaleop.core.data.datasources

import gr.cpaleop.core.datasource.model.response.RemoteCategory
import gr.cpaleop.core.datasource.model.response.RemoteRegisteredCategory
import kotlinx.coroutines.flow.Flow

interface CategoriesDataSource {

    suspend fun fetchPublicCategories(): List<RemoteCategory>

    suspend fun fetchCategories(): List<RemoteCategory>

    suspend fun fetchCategoriesFlow(): Flow<List<RemoteCategory>>

    suspend fun fetchRegisteredCategories(): List<RemoteRegisteredCategory>

    suspend fun fetchCategoryById(id: String?): RemoteCategory

    suspend fun updateRegisteredCategories(registeredCategories: List<String>, nonRegisteredCategories: List<String>)
}