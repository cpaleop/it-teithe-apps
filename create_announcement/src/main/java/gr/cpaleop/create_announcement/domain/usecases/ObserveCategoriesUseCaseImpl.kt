package gr.cpaleop.create_announcement.domain.usecases

import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.create_announcement.domain.repositories.CategoriesRepository
import kotlinx.coroutines.flow.Flow

class ObserveCategoriesUseCaseImpl(private val categoriesRepository: CategoriesRepository) :
    ObserveCategoriesUseCase {

    override suspend fun invoke(): Flow<List<Category>> {
        return categoriesRepository.getCategoriesFlow()
    }

    override suspend fun refresh() {
        categoriesRepository.refreshCategories()
    }
}