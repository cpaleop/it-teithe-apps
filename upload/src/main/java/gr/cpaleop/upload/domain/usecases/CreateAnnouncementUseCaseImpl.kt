package gr.cpaleop.upload.domain.usecases

import gr.cpaleop.upload.domain.entities.NewAnnouncement
import gr.cpaleop.upload.domain.repositories.AnnouncementsRepository

class CreateAnnouncementUseCaseImpl(private val announcementsRepository: AnnouncementsRepository) :
    CreateAnnouncementUseCase {

    override suspend fun invoke(newAnnouncement: NewAnnouncement) {
        announcementsRepository.createAnnouncement(newAnnouncement)
    }
}