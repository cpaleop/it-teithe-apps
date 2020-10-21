package gr.cpaleop.documents.domain.usecases

import gr.cpaleop.core.domain.entities.DocumentPreview
import gr.cpaleop.documents.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.first

class ToggleDocumentPreviewPreferenceUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    ToggleDocumentPreviewPreferenceUseCase {

    override suspend fun invoke() {
        val newDocumentPreviewPreference = when (val currentDocumentPreviewPreference =
            preferencesRepository.getDocumentPreviewPreferenceFlow().first()) {
            DocumentPreview.FILE -> DocumentPreview.FOLDER
            DocumentPreview.FOLDER -> DocumentPreview.FILE
            else -> throw IllegalArgumentException("Document preview preference $currentDocumentPreviewPreference not found")
        }
        preferencesRepository.updateDocumentPreviewPreference(newDocumentPreviewPreference)
    }
}