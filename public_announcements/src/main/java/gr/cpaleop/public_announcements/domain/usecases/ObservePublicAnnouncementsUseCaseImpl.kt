package gr.cpaleop.public_announcements.domain.usecases

import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.public_announcements.domain.repositories.AnnouncementsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine

@FlowPreview
@ExperimentalCoroutinesApi
class ObservePublicAnnouncementsUseCaseImpl(private val announcementsRepository: AnnouncementsRepository) :
    ObservePublicAnnouncementsUseCase {

    private val filterChannel = ConflatedBroadcastChannel("")

    override suspend fun invoke(): Flow<List<Announcement>> {
        return announcementsRepository.getPublicAnnouncementsFlow()
            .combine(filterChannel.asFlow()) { announcementList, filterQuery ->
                if (filterQuery.isEmpty()) return@combine announcementList
                announcementList.filter { announcement ->
                    announcement.title.contains(filterQuery, true) ||
                            announcement.text.contains(filterQuery, true)
                }
            }
    }

    override suspend fun filter(filterQuery: String) {
        filterChannel.send(filterQuery)
    }
}