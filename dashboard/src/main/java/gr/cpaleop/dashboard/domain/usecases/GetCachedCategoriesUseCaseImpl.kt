package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.dashboard.domain.repositories.CategoriesRepository

class GetCachedCategoriesUseCaseImpl(private val categoriesRepository: CategoriesRepository) :
    GetCachedCategoriesUseCase {

    override suspend fun invoke(): List<Category> {
        return categoriesRepository.getCachedCategories()
    }
}