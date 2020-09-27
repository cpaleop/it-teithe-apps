package gr.cpaleop.documents.domain.usecases

import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.documents.domain.entities.DocumentSort
import gr.cpaleop.documents.domain.entities.DocumentSortType
import gr.cpaleop.documents.domain.repositories.PreferencesRepository

class GetDocumentSortOptionsUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    GetDocumentSortOptionsUseCase {

    override suspend fun invoke(): List<DocumentSort> {
        val currentDocumentSort = preferencesRepository.getDocumentSort()

        val documentSortOptions = listOf(
            DocumentSort(
                type = DocumentSortType.DATE,
                selected = false,
                descending = false
            ),
            DocumentSort(
                type = DocumentSortType.ALPHABETICAL,
                selected = false,
                descending = false
            )
        )

        return documentSortOptions
            .mapAsync { if (it.type == currentDocumentSort.type) currentDocumentSort else it }
            .sortedByDescending { it.type }
    }
}