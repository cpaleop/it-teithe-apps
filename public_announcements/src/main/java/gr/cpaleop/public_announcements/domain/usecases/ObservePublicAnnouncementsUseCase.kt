package gr.cpaleop.public_announcements.domain.usecases

import gr.cpaleop.core.domain.entities.Announcement
import kotlinx.coroutines.flow.Flow

interface ObservePublicAnnouncementsUseCase {

    var filter: String

    suspend operator fun invoke(): Flow<List<Announcement>>
}