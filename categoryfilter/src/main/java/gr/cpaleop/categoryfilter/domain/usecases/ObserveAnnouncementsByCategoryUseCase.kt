package gr.cpaleop.categoryfilter.domain.usecases

import gr.cpaleop.categoryfilter.domain.entities.Announcement
import kotlinx.coroutines.flow.Flow

interface ObserveAnnouncementsByCategoryUseCase {

    operator fun invoke(categoryId: String): Flow<List<Announcement>>

    suspend fun refresh(categoryId: String)

    fun filter(filterQuery: String)
}