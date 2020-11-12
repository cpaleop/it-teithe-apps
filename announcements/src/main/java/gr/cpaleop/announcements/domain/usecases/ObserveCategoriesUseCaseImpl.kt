package gr.cpaleop.announcements.domain.usecases

import gr.cpaleop.announcements.domain.repositories.CategoriesRepository
import gr.cpaleop.core.domain.entities.Category
import kotlinx.coroutines.flow.Flow

class ObserveCategoriesUseCaseImpl(private val categoriesRepository: CategoriesRepository) :
    ObserveCategoriesUseCase {

    override suspend fun invoke(): Flow<List<Category>> {
        return categoriesRepository.getCategoriesFlow()
    }
}