package gr.cpaleop.documents.domain.usecases

import gr.cpaleop.core.domain.entities.DocumentPreview
import gr.cpaleop.documents.domain.repositories.PreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@ExperimentalCoroutinesApi
class ObserveDocumentPreviewPreferenceUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    ObserveDocumentPreviewPreferenceUseCase {

    @DocumentPreview
    override fun invoke(announcementId: String?): Flow<Int> {
        if (announcementId != null) return flow { emit(DocumentPreview.FILE) }
        return preferencesRepository.getDocumentPreviewPreferenceFlow()
    }
}