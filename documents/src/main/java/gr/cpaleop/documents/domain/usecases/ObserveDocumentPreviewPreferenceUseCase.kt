package gr.cpaleop.documents.domain.usecases

import gr.cpaleop.core.domain.entities.DocumentPreview
import kotlinx.coroutines.flow.Flow

interface ObserveDocumentPreviewPreferenceUseCase {

    @DocumentPreview
    operator fun invoke(announcementId: String?): Flow<Int>
}