package gr.cpaleop.dashboard.domain.repositories

import gr.cpaleop.dashboard.domain.entities.Category

interface CategoriesRepository {

    suspend fun getCategories(): List<Category>
}