package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.core.domain.entities.Category

interface GetCachedCategoriesUseCase {

    suspend operator fun invoke(): List<Category>
}