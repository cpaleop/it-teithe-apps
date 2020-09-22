package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.AnnouncementSort

interface UpdateAnnouncementsSortUseCase {

    suspend operator fun invoke(announcementsSort: AnnouncementSort): AnnouncementSort
}