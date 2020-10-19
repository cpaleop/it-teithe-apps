package gr.cpaleop.categoryfilter.domain.usecases

import gr.cpaleop.core.domain.entities.Announcement
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@ExperimentalCoroutinesApi
interface ObserveAnnouncementsByCategoryUseCase {

    val filterStream: StateFlow<String>

    operator fun invoke(categoryId: String): Flow<List<Announcement>>

    suspend fun refresh(categoryId: String)

    fun filter(filterQuery: String)
}