package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.DocumentPreview
import gr.cpaleop.dashboard.domain.repositories.PreferencesRepository

class ToggleDocumentPreviewPreferenceUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    ToggleDocumentPreviewPreferenceUseCase {

    override suspend fun invoke(): Int {
        val newDocumentPreviewPreference = when (val currentDocumentPreviewPreference =
            preferencesRepository.getDocumentPreviewPreference()) {
            DocumentPreview.FILE -> DocumentPreview.FOLDER
            DocumentPreview.FOLDER -> DocumentPreview.FILE
            else -> throw IllegalArgumentException("Document preview preference $currentDocumentPreviewPreference not found")
        }
        preferencesRepository.updateDocumentPreviewPreference(newDocumentPreviewPreference)
        return newDocumentPreviewPreference
    }
}