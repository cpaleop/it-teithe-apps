package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.DocumentSort
import gr.cpaleop.dashboard.domain.entities.DocumentSortType
import gr.cpaleop.dashboard.domain.repositories.PreferencesRepository

class GetDocumentSortOptionsUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    GetDocumentSortOptionsUseCase {

    override suspend fun invoke(): List<DocumentSort> {
        val currentDocumentSort = preferencesRepository.getDocumentSort()
        val notSelectedOption = when (currentDocumentSort.type) {
            DocumentSortType.TYPE_ALPHABETICAL -> DocumentSort(
                type = DocumentSortType.TYPE_DATE,
                selected = false,
                descending = false
            )
            DocumentSortType.TYPE_DATE -> DocumentSort(
                type = DocumentSortType.TYPE_ALPHABETICAL,
                selected = false,
                descending = false
            )
            else -> throw IllegalArgumentException("No sorting type found with value ${currentDocumentSort.type}")
        }

        return listOf(
            currentDocumentSort,
            notSelectedOption
        ).sortedByDescending { it.type }
    }
}