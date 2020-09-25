package gr.cpaleop.dashboard.domain.entities

import androidx.annotation.IntDef

data class DocumentSort(
    @DocumentSortType
    val type: Int,
    val selected: Boolean,
    val descending: Boolean
)

@IntDef(
    DocumentSortType.DATE,
    DocumentSortType.ALPHABETICAL
)
@Retention(AnnotationRetention.SOURCE)
annotation class DocumentSortType {

    companion object {

        const val DATE: Int = -1
        const val ALPHABETICAL: Int = 1
    }
}