package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.DocumentSort

interface GetDocumentSortUseCase {

    suspend operator fun invoke(): DocumentSort
}