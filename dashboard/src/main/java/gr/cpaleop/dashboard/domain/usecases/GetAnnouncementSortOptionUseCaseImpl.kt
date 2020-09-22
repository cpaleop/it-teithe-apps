package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.AnnouncementSort
import gr.cpaleop.dashboard.domain.repositories.PreferencesRepository

class GetAnnouncementSortOptionUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    GetAnnouncementSortOptionUseCase {

    override suspend fun invoke(): AnnouncementSort {
        return preferencesRepository.getAnnouncementSort()
    }
}