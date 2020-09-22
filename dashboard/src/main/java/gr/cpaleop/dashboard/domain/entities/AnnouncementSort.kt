package gr.cpaleop.dashboard.domain.entities

import androidx.annotation.IntDef

data class AnnouncementSort(
    @AnnouncementSortType
    val type: Int,
    val selected: Boolean,
    val descending: Boolean
)

@IntDef(
    AnnouncementSortType.DATE,
    AnnouncementSortType.TITLE
)
@Retention(AnnotationRetention.SOURCE)
annotation class AnnouncementSortType {

    companion object {

        const val DATE = -1
        const val TITLE = 0
    }
}