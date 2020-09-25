package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.DocumentPreview

interface GetDocumentPreviewPreferenceUseCase {

    @DocumentPreview
    suspend operator fun invoke(): Int
}