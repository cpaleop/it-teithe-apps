package gr.cpaleop.create_announcement.domain.repositories

import gr.cpaleop.core.domain.entities.Category

interface CategoriesRepository {

    suspend fun getCategories(): List<Category>

    suspend fun getCategoryById(id: String): Category
}