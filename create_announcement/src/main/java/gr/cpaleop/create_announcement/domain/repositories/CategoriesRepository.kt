package gr.cpaleop.create_announcement.domain.repositories

import gr.cpaleop.core.domain.entities.Category
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {

    suspend fun refreshCategories()

    suspend fun getCategoriesFlow(): Flow<List<Category>>

    suspend fun getCategoryById(id: String): Category
}