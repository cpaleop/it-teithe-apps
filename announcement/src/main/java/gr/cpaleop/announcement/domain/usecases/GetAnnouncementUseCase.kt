package gr.cpaleop.announcement.domain.usecases

import gr.cpaleop.core.domain.entities.Announcement

interface GetAnnouncementUseCase {

    suspend operator fun invoke(id: String): Announcement
}