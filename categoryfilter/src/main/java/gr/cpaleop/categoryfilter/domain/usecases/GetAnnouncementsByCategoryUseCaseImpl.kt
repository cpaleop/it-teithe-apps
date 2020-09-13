package gr.cpaleop.categoryfilter.domain.usecases

import gr.cpaleop.categoryfilter.domain.entities.Announcement
import gr.cpaleop.categoryfilter.domain.repositories.AnnouncementsRepository

class GetAnnouncementsByCategoryUseCaseImpl(private val announcementsRepository: AnnouncementsRepository) :
    GetAnnouncementsByCategoryUseCase {

    override suspend fun invoke(categoryId: String): List<Announcement> {
        return announcementsRepository.getAnnouncementsByCategory(categoryId)
    }
}