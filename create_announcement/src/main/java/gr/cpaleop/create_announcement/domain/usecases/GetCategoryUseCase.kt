package gr.cpaleop.create_announcement.domain.usecases

import gr.cpaleop.core.domain.entities.Category

interface GetCategoryUseCase {

    suspend operator fun invoke(id: String): Category
}