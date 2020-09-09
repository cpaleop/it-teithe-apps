package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.core.domain.entities.Document

interface GetSavedDocumentsUseCase {

    suspend operator fun invoke(): List<Document>
}