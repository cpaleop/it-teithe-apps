package gr.cpaleop.announcement.domain.usecases

import gr.cpaleop.announcement.domain.repositories.AnnouncementsRepository
import gr.cpaleop.core.domain.entities.Announcement

class GetAnnouncementUseCaseImpl(private val announcementsRepository: AnnouncementsRepository) :
    GetAnnouncementUseCase {

    override suspend fun invoke(id: String): Announcement {
        return announcementsRepository.getAnnouncementById(id)
    }
}