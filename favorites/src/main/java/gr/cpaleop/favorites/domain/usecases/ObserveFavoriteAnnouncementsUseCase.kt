package gr.cpaleop.favorites.domain.usecases

import gr.cpaleop.core.domain.entities.Announcement
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
interface ObserveFavoriteAnnouncementsUseCase {

    var filter: String

    suspend operator fun invoke(): Flow<List<Announcement>>
}