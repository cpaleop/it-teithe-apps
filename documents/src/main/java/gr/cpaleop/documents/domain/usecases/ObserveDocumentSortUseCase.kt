package gr.cpaleop.documents.domain.usecases

import gr.cpaleop.documents.domain.entities.DocumentSort
import kotlinx.coroutines.flow.Flow

interface ObserveDocumentSortUseCase {

    suspend operator fun invoke(): Flow<DocumentSort>

    suspend fun update(documentSort: DocumentSort)
}