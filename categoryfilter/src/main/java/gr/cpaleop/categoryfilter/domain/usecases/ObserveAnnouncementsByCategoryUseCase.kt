package gr.cpaleop.categoryfilter.domain.usecases

import gr.cpaleop.core.domain.entities.Announcement
import kotlinx.coroutines.flow.Flow

interface ObserveAnnouncementsByCategoryUseCase {

    var filter: String

    suspend operator fun invoke(categoryId: String): Flow<List<Announcement>>

    suspend fun refresh(categoryId: String)
}