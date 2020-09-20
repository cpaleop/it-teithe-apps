package gr.cpaleop.dashboard.presentation.documents.sort

import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.domain.entities.DocumentSort
import gr.cpaleop.dashboard.domain.entities.DocumentSortType

class DocumentSortOptionMapper {

    operator fun invoke(documentSort: DocumentSort): DocumentSortOption {
        return when (documentSort.type) {
            DocumentSortType.TYPE_DATE -> DocumentSortOption(
                type = documentSort.type,
                label = R.string.documents_sort_date,
                selected = documentSort.selected,
                descending = documentSort.descending
            )
            DocumentSortType.TYPE_ALPHABETICAL -> DocumentSortOption(
                type = documentSort.type,
                label = R.string.documents_sort_alphabetical,
                selected = documentSort.selected,
                descending = documentSort.descending
            )
            else -> throw IllegalArgumentException("No sorting type found with the name ${documentSort.type}")
        }
    }
}