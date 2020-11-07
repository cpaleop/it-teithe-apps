package gr.cpaleop.create_announcement.domain.usecases

import gr.cpaleop.core.domain.entities.Category

interface GetCategoriesUseCase {

    suspend operator fun invoke(): List<Category>
}