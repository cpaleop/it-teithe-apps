package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.dashboard.domain.entities.AnnouncementSort
import gr.cpaleop.dashboard.domain.entities.AnnouncementSortType
import gr.cpaleop.dashboard.domain.repositories.PreferencesRepository

class GetAnnouncementsSortOptionsUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    GetAnnouncementsSortOptionsUseCase {

    override suspend fun invoke(): List<AnnouncementSort> {
        val currentAnnouncementSort = preferencesRepository.getAnnouncementSort()

        val options = listOf(
            AnnouncementSort(
                type = AnnouncementSortType.DATE,
                selected = false,
                descending = false
            ),
            AnnouncementSort(
                type = AnnouncementSortType.TITLE,
                selected = false,
                descending = false
            )
        )

        return options
            .mapAsync { if (it.type == currentAnnouncementSort.type) currentAnnouncementSort else it }
            .sortedByDescending { it.type }
    }
}