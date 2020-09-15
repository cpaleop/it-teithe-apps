package gr.cpaleop.categoryfilter.domain.usecases

import gr.cpaleop.categoryfilter.domain.entities.Announcement
import gr.cpaleop.categoryfilter.domain.repositories.AnnouncementsRepository
import kotlinx.coroutines.flow.Flow

class ObserveAnnouncementsByCategoryUseCaseImpl(private val announcementsRepository: AnnouncementsRepository) :
    ObserveAnnouncementsByCategoryUseCase {

    override suspend fun invoke(categoryId: String): Flow<List<Announcement>> {
        return announcementsRepository.getAnnouncementsByCategory(categoryId)
    }
}