package gr.cpaleop.create_announcement.domain.usecases

import gr.cpaleop.create_announcement.domain.entities.NewAnnouncement
import timber.log.Timber

class CreateAnnouncementUseCaseImpl : CreateAnnouncementUseCase {

    override suspend fun invoke(newAnnouncement: NewAnnouncement) {
        Timber.e("NEW ANNOUNCEMENT: $newAnnouncement")
        return
    }
}