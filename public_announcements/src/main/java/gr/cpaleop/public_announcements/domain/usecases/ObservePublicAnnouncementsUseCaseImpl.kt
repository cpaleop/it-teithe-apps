package gr.cpaleop.public_announcements.domain.usecases

import gr.cpaleop.common.extensions.removeIntonation
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
    override var filter: String
        get() = filterStateFlow.value
        set(value) {
            filterStateFlow.value = value
        }

    override suspend fun invoke(): Flow<List<Announcement>> {
        return announcementsRepository.getPublicAnnouncementsFlow()
            .combine(filterStateFlow) { announcementList, filterQuery ->
                if (filterQuery.isEmpty()) return@combine announcementList
                announcementList.filter { announcement ->
                    announcement.title.removeIntonation()
                        .contains(filterQuery.removeIntonation(), true) ||
                            announcement.text.removeIntonation()
                                .contains(filterQuery.removeIntonation(), true)
                }
            }
    }
}