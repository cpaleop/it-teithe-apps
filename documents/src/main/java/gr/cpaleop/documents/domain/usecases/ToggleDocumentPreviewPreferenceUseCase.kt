package gr.cpaleop.documents.domain.usecases

import gr.cpaleop.core.domain.entities.DocumentPreview

interface ToggleDocumentPreviewPreferenceUseCase {

    @DocumentPreview
    suspend operator fun invoke(): Int
}