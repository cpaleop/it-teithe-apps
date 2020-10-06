package gr.cpaleop.documents.domain.usecases

import gr.cpaleop.core.domain.entities.DocumentSort

interface GetDocumentSortOptionsUseCase {

    suspend operator fun invoke(): List<DocumentSort>
}