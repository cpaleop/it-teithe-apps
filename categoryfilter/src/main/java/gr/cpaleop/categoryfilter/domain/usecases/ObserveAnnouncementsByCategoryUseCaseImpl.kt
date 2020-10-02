package gr.cpaleop.categoryfilter.domain.usecases

import gr.cpaleop.categoryfilter.domain.entities.Announcement
import gr.cpaleop.categoryfilter.domain.repositories.AnnouncementsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine

@ExperimentalCoroutinesApi
class ObserveAnnouncementsByCategoryUseCaseImpl(private val announcementsRepository: AnnouncementsRepository) :
    ObserveAnnouncementsByCategoryUseCase {

    @ExperimentalCoroutinesApi
    private val _filterChannel = MutableStateFlow("")
    private val filterChannel: StateFlow<String> = _filterChannel

    override fun invoke(categoryId: String): Flow<List<Announcement>> {
        return announcementsRepository.getCachedAnnouncementsByCategoryFlow(categoryId)
            .combine(filterChannel) { announcements, filterQuery ->
                if (filterQuery.isEmpty()) return@combine announcements
                announcements.filter { announcement ->
                    announcement.title.contains(filterQuery, true) ||
                            announcement.text.contains(filterQuery, true) ||
                            announcement.publisherName.contains(filterQuery, true)
                }
            }
    }

    override suspend fun refresh(categoryId: String) {
        announcementsRepository.updateCachedAnnouncementsByCategoryFlow(categoryId)
    }

    override fun filter(filterQuery: String) {
        _filterChannel.value = filterQuery
    }
}