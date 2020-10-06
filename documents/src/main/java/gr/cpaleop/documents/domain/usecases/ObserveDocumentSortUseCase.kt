package gr.cpaleop.documents.domain.usecases

import gr.cpaleop.core.domain.entities.DocumentSort
import kotlinx.coroutines.flow.Flow

interface ObserveDocumentSortUseCase {

    operator fun invoke(): Flow<DocumentSort>

    suspend fun update(documentSort: DocumentSort)
}