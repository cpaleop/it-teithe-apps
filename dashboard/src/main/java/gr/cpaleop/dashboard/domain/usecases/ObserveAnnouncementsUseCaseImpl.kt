package gr.cpaleop.dashboard.domain.usecases

import androidx.paging.PagingData
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.dashboard.domain.repositories.AnnouncementsRepository
import kotlinx.coroutines.flow.Flow

class ObserveAnnouncementsUseCaseImpl(private val announcementsRepository: AnnouncementsRepository) :
    ObserveAnnouncementsUseCase {

    override suspend fun invoke(): Flow<PagingData<Announcement>> {
        return announcementsRepository.getAnnouncements()
    }
}