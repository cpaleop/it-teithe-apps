package gr.cpaleop.categoryfilter.domain.usecases

import gr.cpaleop.categoryfilter.domain.repositories.CategoriesRepository

class GetCategoryNameUseCaseImpl(private val categoriesRepository: CategoriesRepository) :
    GetCategoryNameUseCase {

    override suspend fun invoke(categoryId: String): String {
        return categoriesRepository.getCategoryNameById(categoryId)
    }
}