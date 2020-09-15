package gr.cpaleop.categoryfilter.domain.usecases

import gr.cpaleop.categoryfilter.domain.entities.Announcement
import kotlinx.coroutines.flow.Flow

interface ObserveAnnouncementsByCategoryUseCase {

    suspend operator fun invoke(categoryId: String): Flow<List<Announcement>>
}