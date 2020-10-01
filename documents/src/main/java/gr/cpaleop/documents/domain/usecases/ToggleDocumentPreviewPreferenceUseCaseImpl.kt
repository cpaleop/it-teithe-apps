package gr.cpaleop.documents.domain.usecases

import gr.cpaleop.documents.domain.entities.DocumentPreview
import gr.cpaleop.documents.domain.repositories.PreferencesRepository

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