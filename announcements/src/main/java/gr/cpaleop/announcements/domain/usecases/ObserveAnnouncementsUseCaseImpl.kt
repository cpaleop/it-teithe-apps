package gr.cpaleop.announcements.domain.usecases

import androidx.paging.PagingData
import gr.cpaleop.announcements.domain.repositories.AnnouncementsRepository
import gr.cpaleop.core.domain.entities.Announcement
import kotlinx.coroutines.flow.Flow

class ObserveAnnouncementsUseCaseImpl(private val announcementsRepository: AnnouncementsRepository) :
    ObserveAnnouncementsUseCase {

    override suspend fun invoke(): Flow<PagingData<Announcement>> {
        return announcementsRepository.getAnnouncements()
    }
}