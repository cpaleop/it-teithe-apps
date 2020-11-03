package gr.cpaleop.favorites.domain.usecases

import gr.cpaleop.common.extensions.removeIntonation
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.favorites.domain.repositories.AnnouncementsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

@ExperimentalCoroutinesApi
class ObserveFavoriteAnnouncementsUseCaseImpl(private val announcementsRepository: AnnouncementsRepository) :
    ObserveFavoriteAnnouncementsUseCase {

    private val filterFlow = MutableStateFlow("")

    override fun invoke(): Flow<List<Announcement>> {
        return announcementsRepository.getFavoritesFlow()
            .combine(filterFlow) { announcementList, filter ->
                if (filter.isEmpty()) return@combine announcementList
                return@combine announcementList.filter { announcement ->
                    announcement.title.removeIntonation()
                        .contains(filter.removeIntonation(), true) ||
                            announcement.text.removeIntonation()
                                .contains(filter.removeIntonation(), true) ||
                            announcement.publisherName.removeIntonation()
                                .contains(filter.removeIntonation(), true)
                }
            }
    }

    override fun filter(filterQuery: String) {
        filterFlow.value = filterQuery
    }
}