package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.dashboard.domain.repositories.AnnouncementsRepository

class SearchAnnouncementUseCaseImpl(private val announcementsRepository: AnnouncementsRepository) :
    SearchAnnouncementUseCase {

    override suspend fun invoke(query: String): List<Announcement> {
        return announcementsRepository.getAnnouncementsSearchResults(query)
    }
}