package gr.cpaleop.create_announcement.domain.usecases

import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.create_announcement.domain.repositories.CategoriesRepository

class GetCategoryUseCaseImpl(private val categoriesRepository: CategoriesRepository) :
    GetCategoryUseCase {

    override suspend fun invoke(id: String): Category {
        return categoriesRepository.getCategoryById(id)
    }
}