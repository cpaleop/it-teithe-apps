package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.Category

interface GetCategoriesUseCase {

    suspend operator fun invoke(): List<Category>
}