package gr.cpaleop.dashboard.domain.usecases

import androidx.paging.PagingData
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.dashboard.domain.repositories.AnnouncementsRepository
import gr.cpaleop.dashboard.domain.repositories.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class ObserveAnnouncementsUseCaseImpl(
    private val announcementsRepository: AnnouncementsRepository,
    private val preferencesRepository: PreferencesRepository
) : ObserveAnnouncementsUseCase {

    override suspend fun invoke(coroutineScope: CoroutineScope): Flow<PagingData<Announcement>> {
        announcementsRepository.sort(preferencesRepository.getAnnouncementSort())
        return announcementsRepository.getAnnouncements(coroutineScope)
    }
}