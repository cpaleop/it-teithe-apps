package gr.cpaleop.create_announcement.domain.behavior

import gr.cpaleop.create_announcement.domain.entities.EmptyTextException
import gr.cpaleop.create_announcement.domain.entities.EmptyTitleException
import gr.cpaleop.upload.domain.entities.NewAnnouncement

class AnnouncementValidatorImpl : AnnouncementValidator {

    override fun invoke(newAnnouncement: NewAnnouncement): NewAnnouncement {
        if (newAnnouncement.title.gr.isBlank() || newAnnouncement.title.gr.isEmpty() ||
            newAnnouncement.title.en.isBlank() || newAnnouncement.title.en.isEmpty()
        ) throw EmptyTitleException()

        if (newAnnouncement.text.gr.isBlank() || newAnnouncement.text.gr.isEmpty() ||
            newAnnouncement.text.en.isBlank() || newAnnouncement.text.en.isEmpty()
        ) throw EmptyTextException()

        if (newAnnouncement.category.isEmpty() || newAnnouncement.category.isBlank())
            throw EmptyTextException()

        return newAnnouncement
    }
}