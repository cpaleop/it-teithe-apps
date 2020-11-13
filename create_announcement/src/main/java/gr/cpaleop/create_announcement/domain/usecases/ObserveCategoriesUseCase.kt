package gr.cpaleop.create_announcement.domain.usecases

import gr.cpaleop.core.domain.entities.Category
import kotlinx.coroutines.flow.Flow

interface ObserveCategoriesUseCase {

    suspend operator fun invoke(): Flow<List<Category>>

    suspend fun refresh()
}