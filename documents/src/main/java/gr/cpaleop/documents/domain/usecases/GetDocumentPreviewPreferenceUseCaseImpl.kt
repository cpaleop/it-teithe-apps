package gr.cpaleop.documents.domain.usecases

import gr.cpaleop.core.domain.entities.DocumentPreview
import gr.cpaleop.documents.domain.repositories.PreferencesRepository

class GetDocumentPreviewPreferenceUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    GetDocumentPreviewPreferenceUseCase {

    @DocumentPreview
    override suspend fun invoke(announcementId: String?): Int {
        return if (announcementId != null) DocumentPreview.FILE
        else preferencesRepository.getDocumentPreviewPreference()
    }
}