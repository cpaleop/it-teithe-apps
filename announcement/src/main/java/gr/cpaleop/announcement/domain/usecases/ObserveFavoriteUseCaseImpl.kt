package gr.cpaleop.announcement.domain.usecases

import gr.cpaleop.announcement.domain.repositories.AnnouncementsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveFavoriteUseCaseImpl(private val announcementsRepository: AnnouncementsRepository) :
    ObserveFavoriteUseCase {

    override fun invoke(announcementId: String): Flow<Boolean> {
        return announcementsRepository.getSavedAnnouncementByIdFlow(announcementId)
            .map { savedAnnouncement ->
                savedAnnouncement != null
            }
    }
}