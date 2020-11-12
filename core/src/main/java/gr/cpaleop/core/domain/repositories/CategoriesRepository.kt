package gr.cpaleop.core.domain.repositories

import gr.cpaleop.core.domain.entities.Category
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {

    suspend fun getCategories(): List<Category>

    suspend fun getCategoriesFlow(): Flow<List<Category>>

    suspend fun getRegisteredCategories(): List<Category>

    suspend fun getRegisteredCategoriesFlow(): Flow<List<Category>>
}