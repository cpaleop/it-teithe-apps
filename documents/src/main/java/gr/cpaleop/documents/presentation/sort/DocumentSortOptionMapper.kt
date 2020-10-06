package gr.cpaleop.documents.presentation.sort

import gr.cpaleop.core.domain.entities.DocumentSort
import gr.cpaleop.core.domain.entities.DocumentSortType
import gr.cpaleop.documents.R

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