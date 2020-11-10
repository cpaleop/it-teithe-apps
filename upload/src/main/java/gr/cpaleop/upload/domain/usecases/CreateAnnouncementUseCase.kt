package gr.cpaleop.upload.domain.usecases

import gr.cpaleop.upload.domain.entities.NewAnnouncement

interface CreateAnnouncementUseCase {

    suspend operator fun invoke(newAnnouncement: NewAnnouncement)
}