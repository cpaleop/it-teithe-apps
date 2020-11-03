package gr.cpaleop.favorites.domain.usecases

import gr.cpaleop.core.domain.entities.Announcement
import kotlinx.coroutines.flow.Flow

interface ObserveFavoriteAnnouncementsUseCase {

    operator fun invoke(): Flow<List<Announcement>>

    fun filter(filterQuery: String)
}