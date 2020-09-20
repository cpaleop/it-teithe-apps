package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.DocumentOptionType

interface GetDocumentOptionsUseCase {

    operator fun invoke(): List<DocumentOptionType>
}