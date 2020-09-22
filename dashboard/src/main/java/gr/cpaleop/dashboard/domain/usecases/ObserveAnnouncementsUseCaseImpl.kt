package gr.cpaleop.dashboard.domain.usecases

import androidx.paging.PagingData
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.dashboard.domain.repositories.AnnouncementsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@FlowPreview
@ExperimentalCoroutinesApi
class ObserveAnnouncementsUseCaseImpl(private val announcementsRepository: AnnouncementsRepository) :
    ObserveAnnouncementsUseCase {

    /*private val filterChannel = ConflatedBroadcastChannel("")
    private val sortChannel = ConflatedBroadcastChannel(
        AnnouncementSort(
            AnnouncementSortType.DATE,
            selected = true,
            descending = true
        )
    )*/

    override suspend fun invoke(coroutineScope: CoroutineScope): Flow<PagingData<Announcement>> {
        /*filterChannel
            .asFlow()
            .debounce(300)
            .onEach { announcementsRepository.invalidateDataSource() }
            .launchIn(coroutineScope)

        sortChannel
            .asFlow()
            .onEach { announcementsRepository.invalidateDataSource() }
            .launchIn(coroutineScope)*/

        return announcementsRepository.getAnnouncements(coroutineScope)
    }

    /*override suspend fun filter(filter: String) {
        filterChannel.send(filter)
    }

    override suspend fun sort(announcementsSort: AnnouncementSort) {
        sortChannel.send(announcementsSort)
    }*/
}