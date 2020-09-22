package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.dashboard.domain.entities.DocumentSort
import gr.cpaleop.dashboard.domain.entities.DocumentSortType
import gr.cpaleop.dashboard.domain.repositories.PreferencesRepository

class GetDocumentSortOptionsUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    GetDocumentSortOptionsUseCase {

    override suspend fun invoke(): List<DocumentSort> {
        val currentDocumentSort = preferencesRepository.getDocumentSort()

        val documentSortOptions = listOf(
            DocumentSort(
                type = DocumentSortType.TYPE_DATE,
                selected = false,
                descending = false
            ),
            DocumentSort(
                type = DocumentSortType.TYPE_ALPHABETICAL,
                selected = false,
                descending = false
            )
        )

        return documentSortOptions
            .mapAsync { if (it.type == currentDocumentSort.type) currentDocumentSort else it }
            .sortedByDescending { it.type }
    }
}