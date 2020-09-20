package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.DocumentSort
import gr.cpaleop.dashboard.domain.repositories.PreferencesRepository

class GetDocumentSortUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    GetDocumentSortUseCase {

    override suspend fun invoke(): DocumentSort {
        return preferencesRepository.getDocumentSort()
    }
}