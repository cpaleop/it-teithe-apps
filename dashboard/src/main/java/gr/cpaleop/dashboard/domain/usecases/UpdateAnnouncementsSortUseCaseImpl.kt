package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.AnnouncementSort
import gr.cpaleop.dashboard.domain.repositories.AnnouncementsRepository
import gr.cpaleop.dashboard.domain.repositories.PreferencesRepository

class UpdateAnnouncementsSortUseCaseImpl(
    private val announcementsRepository: AnnouncementsRepository,
    private val preferencesRepository: PreferencesRepository
) : UpdateAnnouncementsSortUseCase {

    override suspend fun invoke(announcementsSort: AnnouncementSort): AnnouncementSort {
        preferencesRepository.updateAnnouncementSort(announcementsSort)
        announcementsRepository.sort(announcementsSort)
        return preferencesRepository.getAnnouncementSort()
    }
}