package gr.cpaleop.dashboard.domain.usecases

import androidx.paging.PagingData
import gr.cpaleop.core.domain.entities.Announcement
import kotlinx.coroutines.flow.Flow

interface ObserveAnnouncementsUseCase {

    suspend operator fun invoke(): Flow<PagingData<Announcement>>
}