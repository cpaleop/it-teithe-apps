package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.DocumentPreview
import gr.cpaleop.dashboard.domain.repositories.PreferencesRepository

class GetDocumentPreviewPreferenceUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    GetDocumentPreviewPreferenceUseCase {

    @DocumentPreview
    override suspend fun invoke(): Int {
        return preferencesRepository.getDocumentPreviewPreference()
    }
}