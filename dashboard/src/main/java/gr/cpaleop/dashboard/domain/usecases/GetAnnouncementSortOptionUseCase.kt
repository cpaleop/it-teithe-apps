package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.AnnouncementSort

interface GetAnnouncementSortOptionUseCase {

    suspend operator fun invoke(): AnnouncementSort
}