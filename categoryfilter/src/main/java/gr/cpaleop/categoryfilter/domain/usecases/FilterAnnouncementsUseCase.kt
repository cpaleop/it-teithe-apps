package gr.cpaleop.categoryfilter.domain.usecases

import gr.cpaleop.categoryfilter.domain.entities.Announcement

interface FilterAnnouncementsUseCase {

    suspend operator fun invoke(
        announcements: List<Announcement>,
        query: String
    ): List<Announcement>
}