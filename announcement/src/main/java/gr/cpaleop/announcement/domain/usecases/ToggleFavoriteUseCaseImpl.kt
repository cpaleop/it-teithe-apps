package gr.cpaleop.announcement.domain.usecases

import gr.cpaleop.announcement.domain.repositories.AnnouncementsRepository
import timber.log.Timber

class ToggleFavoriteUseCaseImpl(private val announcementsRepository: AnnouncementsRepository) :
    ToggleFavoriteUseCase {

    override suspend fun invoke(id: String) {
        if (!announcementsRepository.isAnnouncementSaved(id)) {
            Timber.e("SAVING")
            announcementsRepository.saveAnnouncement(id)
        } else {
            Timber.e("REMOVING")
            announcementsRepository.removeAnnouncement(id)
        }
    }
}