package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.DocumentSort
import gr.cpaleop.dashboard.domain.repositories.PreferencesRepository

class UpdateDocumentSortUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    UpdateDocumentSortUseCase {

    override suspend fun invoke(documentSort: DocumentSort): DocumentSort {
        preferencesRepository.updateDocumentSort(documentSort)
        return preferencesRepository.getDocumentSort()
    }
}