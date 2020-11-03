package gr.cpaleop.announcement.domain.usecases

import gr.cpaleop.announcement.domain.repositories.AnnouncementsRepository

class ToggleFavoriteUseCaseImpl(private val announcementsRepository: AnnouncementsRepository) :
    ToggleFavoriteUseCase {

    override suspend fun invoke(id: String) {
        if (!announcementsRepository.isAnnouncementSaved(id)) {
            announcementsRepository.saveAnnouncement(id)
        } else {
            announcementsRepository.removeAnnouncement(id)
        }
    }
}