package gr.cpaleop.categoryfilter.domain.repositories

interface CategoriesRepository {

    suspend fun getCategoryNameById(categoryId: String): String
}