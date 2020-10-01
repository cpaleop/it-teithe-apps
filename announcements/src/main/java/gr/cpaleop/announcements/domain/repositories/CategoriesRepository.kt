package gr.cpaleop.announcements.domain.repositories

import gr.cpaleop.core.domain.entities.Category

interface CategoriesRepository {

    suspend fun getCategories(): List<Category>

    suspend fun getCachedCategories(): List<Category>

    suspend fun updateRegisteredCategories(
        registeredCategories: List<String>,
        nonRegisteredCategories: List<String>
    )
}