package gr.cpaleop.dashboard.presentation.announcements.options

import androidx.annotation.StringRes
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.domain.entities.AnnouncementSort
import gr.cpaleop.dashboard.domain.entities.AnnouncementSortType

class AnnouncementSortOptionMapper {

    operator fun invoke(announcementSort: AnnouncementSort): AnnouncementSortOption {
        @StringRes
        val labelResource = when (announcementSort.type) {
            AnnouncementSortType.DATE -> R.string.option_sort_by_date_published
            AnnouncementSortType.TITLE -> R.string.option_sort_by_title
            else -> throw IllegalArgumentException("No sort type found with value ${announcementSort.type}")
        }

        return AnnouncementSortOption(
            labelResource = labelResource,
            type = announcementSort.type,
            selected = announcementSort.selected,
            descending = announcementSort.descending
        )
    }
}