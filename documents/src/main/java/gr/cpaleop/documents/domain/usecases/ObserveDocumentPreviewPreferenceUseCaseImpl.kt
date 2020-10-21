package gr.cpaleop.documents.domain.usecases

import gr.cpaleop.core.domain.entities.DocumentPreview
import gr.cpaleop.documents.domain.repositories.PreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

@ExperimentalCoroutinesApi
class ObserveDocumentPreviewPreferenceUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    ObserveDocumentPreviewPreferenceUseCase {

    private val announcementFolderPreviewFlow = flow { emit(DocumentPreview.FILE) }

    @DocumentPreview
    override fun invoke(announcementId: String?): Flow<Int> {
        return preferencesRepository.getDocumentPreviewPreferenceFlow()
            .combine(announcementFolderPreviewFlow) { preference, previewFile ->
                if (announcementId != null) return@combine previewFile
                else preference
            }
    }
}