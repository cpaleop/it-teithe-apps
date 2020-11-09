package gr.cpaleop.create_announcement.domain.usecases

import gr.cpaleop.create_announcement.domain.entities.NewAnnouncement

class CreateAnnouncementUseCaseImpl : CreateAnnouncementUseCase {

    override suspend fun invoke(newAnnouncement: NewAnnouncement) {
        return
    }
}