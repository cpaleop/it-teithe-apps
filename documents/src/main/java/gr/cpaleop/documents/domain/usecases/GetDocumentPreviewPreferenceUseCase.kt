package gr.cpaleop.documents.domain.usecases

import gr.cpaleop.documents.domain.entities.DocumentPreview

interface GetDocumentPreviewPreferenceUseCase {

    @DocumentPreview
    suspend operator fun invoke(announcementId: String?): Int
}