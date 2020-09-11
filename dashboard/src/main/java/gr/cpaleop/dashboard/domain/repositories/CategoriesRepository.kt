package gr.cpaleop.dashboard.domain.repositories

import gr.cpaleop.dashboard.domain.entities.Category

interface CategoriesRepository {

    suspend fun getCategories(): List<Category>

    suspend fun updateRegisteredCategories(
        registeredCategories: List<String>,
        nonRegisteredCategories: List<String>
    )
}