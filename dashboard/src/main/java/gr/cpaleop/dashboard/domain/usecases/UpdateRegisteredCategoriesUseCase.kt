package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.Category

interface UpdateRegisteredCategoriesUseCase {

    suspend operator fun invoke(
        registeredCategories: List<String>,
        nonRegisteredCategories: List<String>
    ): List<Category>
}