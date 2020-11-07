package gr.cpaleop.create_announcement.domain.usecases

import gr.cpaleop.create_announcement.domain.entities.EmptyCategoryException
import gr.cpaleop.create_announcement.domain.entities.EmptyTextException
import gr.cpaleop.create_announcement.domain.entities.EmptyTitleException
import gr.cpaleop.create_announcement.domain.entities.NewAnnouncement

interface CreateAnnouncementUseCase {

    @Throws(EmptyTitleException::class, EmptyTextException::class, EmptyCategoryException::class)
    suspend operator fun invoke(newAnnouncement: NewAnnouncement)
}