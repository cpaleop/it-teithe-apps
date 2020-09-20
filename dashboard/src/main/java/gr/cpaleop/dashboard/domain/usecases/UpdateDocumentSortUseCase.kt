package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.DocumentSort

interface UpdateDocumentSortUseCase {

    suspend operator fun invoke(documentSort: DocumentSort): DocumentSort
}