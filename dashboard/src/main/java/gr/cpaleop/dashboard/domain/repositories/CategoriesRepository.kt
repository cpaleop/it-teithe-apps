package gr.cpaleop.dashboard.domain.repositories

import gr.cpaleop.core.domain.entities.Category
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {

    suspend fun refreshCategories()

    suspend fun getCategoriesFlow(): Flow<List<Category>>

    suspend fun updateRegisteredCategories(
        registeredCategories: List<String>,
        nonRegisteredCategories: List<String>
    )
}