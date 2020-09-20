package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.DocumentSort

interface GetDocumentSortOptionsUseCase {

    suspend operator fun invoke(): List<DocumentSort>
}