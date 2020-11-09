package gr.cpaleop.create_announcement.domain.usecases

import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.create_announcement.domain.repositories.CategoriesRepository

class GetCategoriesUseCaseImpl(private val categoriesRepository: CategoriesRepository) :
    GetCategoriesUseCase {

    override suspend fun invoke(): List<Category> {
        return categoriesRepository.getCategories()
    }
}