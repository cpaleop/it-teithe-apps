package gr.cpaleop.documents.domain.usecases

import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.core.domain.entities.DocumentSort
import gr.cpaleop.core.domain.entities.DocumentSortType
import gr.cpaleop.documents.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.first

class GetDocumentSortOptionsUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    GetDocumentSortOptionsUseCase {

    override suspend fun invoke(): List<DocumentSort> {
        val currentDocumentSort = preferencesRepository.getDocumentSortFlow().first()

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