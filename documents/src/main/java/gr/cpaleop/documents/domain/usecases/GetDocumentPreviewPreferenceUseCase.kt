package gr.cpaleop.documents.domain.usecases

import gr.cpaleop.core.domain.entities.DocumentPreview

interface GetDocumentPreviewPreferenceUseCase {

    @DocumentPreview
    suspend operator fun invoke(announcementId: String?): Int
}