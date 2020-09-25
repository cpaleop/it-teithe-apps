package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.DocumentPreview

interface ToggleDocumentPreviewPreferenceUseCase {

    @DocumentPreview
    suspend operator fun invoke(): Int
}