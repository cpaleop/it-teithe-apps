package gr.cpaleop.favorites.data

import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.favorites.domain.repositories.AnnouncementsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AnnouncementsRepositoryImpl : AnnouncementsRepository {

    override fun getFavoritesFlow(): Flow<List<Announcement>> {
        return flow { emit(emptyList<Announcement>()) }
    }
}