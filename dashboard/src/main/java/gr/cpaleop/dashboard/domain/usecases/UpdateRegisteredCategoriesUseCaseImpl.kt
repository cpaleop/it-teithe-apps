package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.dashboard.domain.repositories.CategoriesRepository

class UpdateRegisteredCategoriesUseCaseImpl(private val categoriesRepository: CategoriesRepository) :
    UpdateRegisteredCategoriesUseCase {

    override suspend fun invoke(
        registeredCategories: List<String>,
        nonRegisteredCategories: List<String>
    ): List<Category> {
        categoriesRepository.updateRegisteredCategories(
            registeredCategories,
            nonRegisteredCategories
        )
        return categoriesRepository.getCategories()
    }
}