package gr.cpaleop.categoryfilter.domain.usecases

import gr.cpaleop.categoryfilter.domain.repositories.AnnouncementsRepository
import gr.cpaleop.common.extensions.removeIntonation
import gr.cpaleop.core.domain.entities.Announcement
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine

@ExperimentalCoroutinesApi
class ObserveAnnouncementsByCategoryUseCaseImpl(private val announcementsRepository: AnnouncementsRepository) :
    ObserveAnnouncementsByCategoryUseCase {

    private val _filterStream: MutableStateFlow<String> = MutableStateFlow("")
    override val filterStream: StateFlow<String>
        get() = _filterStream

    override fun invoke(categoryId: String): Flow<List<Announcement>> {
        return announcementsRepository.getCachedAnnouncementsByCategoryFlow(categoryId)
            .combine(filterStream) { announcements, filterQuery ->
                if (filterQuery.isEmpty()) return@combine announcements
                announcements.filter { announcement ->
                    announcement.title.removeIntonation()
                        .contains(filterQuery.removeIntonation(), true) ||
                            announcement.text.removeIntonation()
                                .contains(filterQuery.removeIntonation(), true) ||
                            announcement.publisherName.removeIntonation()
                                .contains(filterQuery.removeIntonation(), true)
                }
            }
    }

    override suspend fun refresh(categoryId: String) {
        announcementsRepository.updateCachedAnnouncementsByCategoryFlow(categoryId)
    }

    override fun filter(filterQuery: String) {
        _filterStream.value = filterQuery
    }
}