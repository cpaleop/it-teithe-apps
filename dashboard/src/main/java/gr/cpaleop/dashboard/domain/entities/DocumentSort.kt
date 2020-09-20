package gr.cpaleop.dashboard.domain.entities

import androidx.annotation.IntDef

data class DocumentSort(
    @DocumentSortType
    val type: Int,
    val selected: Boolean,
    val descending: Boolean
)

@IntDef(
    DocumentSortType.TYPE_DATE,
    DocumentSortType.TYPE_ALPHABETICAL
)
@Retention(AnnotationRetention.SOURCE)
annotation class DocumentSortType {

    companion object {

        const val TYPE_DATE: Int = -1
        const val TYPE_ALPHABETICAL: Int = 1
    }
}