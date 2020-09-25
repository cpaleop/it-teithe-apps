package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.DocumentSort
import kotlinx.coroutines.flow.Flow

interface ObserveDocumentSortUseCase {

    suspend operator fun invoke(): Flow<DocumentSort>

    suspend fun update(documentSort: DocumentSort)
}