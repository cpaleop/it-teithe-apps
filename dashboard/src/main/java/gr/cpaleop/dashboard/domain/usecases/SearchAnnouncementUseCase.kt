package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.core.domain.entities.Announcement

interface SearchAnnouncementUseCase {

    suspend operator fun invoke(query: String): List<Announcement>
}