package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.Category
import gr.cpaleop.dashboard.domain.repositories.CategoriesRepository

class GetCategoriesUseCaseImpl(private val categoriesRepository: CategoriesRepository) :
    GetCategoriesUseCase {

    override suspend fun invoke(): List<Category> {
        return categoriesRepository.getCategories()
    }
}