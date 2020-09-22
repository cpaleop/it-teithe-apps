package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.repositories.AnnouncementsRepository

class FilterAnnouncementsUseCaseImpl(private val announcementsRepository: AnnouncementsRepository) :
    FilterAnnouncementsUseCase {

    override suspend fun invoke(filter: String) {
        announcementsRepository.filter(filter)
    }
}