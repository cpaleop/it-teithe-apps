package gr.cpaleop.create_announcement.domain

import gr.cpaleop.create_announcement.domain.entities.EmptyCategoryException
import gr.cpaleop.create_announcement.domain.entities.EmptyTextException
import gr.cpaleop.create_announcement.domain.entities.EmptyTitleException
import gr.cpaleop.upload.domain.entities.NewAnnouncement

interface AnnouncementValidator {

    @Throws(EmptyTitleException::class, EmptyTextException::class, EmptyCategoryException::class)
    operator fun invoke(newAnnouncement: NewAnnouncement): Boolean
}