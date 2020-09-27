package gr.cpaleop.announcements.domain.usecases

import gr.cpaleop.announcements.domain.repositories.AnnouncementsRepository

class FilterAnnouncementsUseCaseImpl(private val announcementsRepository: AnnouncementsRepository) :
    FilterAnnouncementsUseCase {

    override suspend fun invoke(filter: String) {
        announcementsRepository.filter(filter)
    }
}