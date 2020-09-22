package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.AnnouncementSort

interface GetAnnouncementsSortOptionsUseCase {

    suspend operator fun invoke(): List<AnnouncementSort>
}