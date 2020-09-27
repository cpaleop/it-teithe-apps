package gr.cpaleop.announcements.domain.usecases

import gr.cpaleop.announcements.domain.repositories.CategoriesRepository
import gr.cpaleop.core.domain.entities.Category

class GetCachedCategoriesUseCaseImpl(private val categoriesRepository: CategoriesRepository) :
    GetCachedCategoriesUseCase {

    override suspend fun invoke(): List<Category> {
        return categoriesRepository.getCachedCategories()
    }
}