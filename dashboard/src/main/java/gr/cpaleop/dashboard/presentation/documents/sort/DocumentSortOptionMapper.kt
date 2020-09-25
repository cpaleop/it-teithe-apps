package gr.cpaleop.dashboard.presentation.documents.sort

import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.domain.entities.DocumentSort
import gr.cpaleop.dashboard.domain.entities.DocumentSortType

class DocumentSortOptionMapper {

    operator fun invoke(documentSort: DocumentSort): DocumentSortOption {
        return when (documentSort.type) {
            DocumentSortType.DATE -> DocumentSortOption(
                type = documentSort.type,
                imageResource = if (documentSort.descending) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up,
                labelResource = R.string.documents_sort_date,
                selected = documentSort.selected,
                descending = documentSort.descending
            )
            DocumentSortType.ALPHABETICAL -> DocumentSortOption(
                type = documentSort.type,
                imageResource = if (documentSort.descending) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up,
                labelResource = R.string.documents_sort_alphabetical,
                selected = documentSort.selected,
                descending = documentSort.descending
            )
            else -> throw IllegalArgumentException("No sorting type found with the name ${documentSort.type}")
        }
    }
}