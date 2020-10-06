package gr.cpaleop.documents.domain.usecases

import gr.cpaleop.core.domain.entities.DocumentSort
import gr.cpaleop.documents.domain.repositories.PreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class ObserveDocumentSortUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    ObserveDocumentSortUseCase {

    override fun invoke(): Flow<DocumentSort> {
        return preferencesRepository.getDocumentSortFlow()
    }

    override suspend fun update(documentSort: DocumentSort) {
        preferencesRepository.updateDocumentSort(documentSort)
    }
}