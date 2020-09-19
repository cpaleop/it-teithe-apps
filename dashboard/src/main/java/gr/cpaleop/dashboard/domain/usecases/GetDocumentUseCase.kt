package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.core.domain.entities.Document

interface GetDocumentUseCase {

    suspend operator fun invoke(uri: String): Document
}