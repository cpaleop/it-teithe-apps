package gr.cpaleop.public_announcements.domain.usecases

import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.network.connection.NoConnectionException
import kotlinx.coroutines.flow.Flow

interface ObservePublicAnnouncementsUseCase {

    @Throws(Throwable::class, NoConnectionException::class)
    suspend operator fun invoke(): Flow<List<Announcement>>

    fun filter(filterQuery: String)
}