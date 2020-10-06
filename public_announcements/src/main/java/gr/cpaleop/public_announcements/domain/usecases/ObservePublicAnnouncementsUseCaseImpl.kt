package gr.cpaleop.public_announcements.domain.usecases

import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.public_announcements.domain.repositories.AnnouncementsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

@ExperimentalCoroutinesApi
class ObservePublicAnnouncementsUseCaseImpl(private val announcementsRepository: AnnouncementsRepository) :
    ObservePublicAnnouncementsUseCase {

    private val filterStateFlow = MutableStateFlow("")

    override suspend fun invoke(): Flow<List<Announcement>> {
        return announcementsRepository.getPublicAnnouncementsFlow()
            .combine(filterStateFlow) { announcementList, filterQuery ->
                if (filterQuery.isEmpty()) return@combine announcementList
                announcementList.filter { announcement ->
                    announcement.title.contains(filterQuery, true) ||
                            announcement.text.contains(filterQuery, true)
                }
            }
    }

    override suspend fun filter(filterQuery: String) {
        filterStateFlow.value = filterQuery
    }
}