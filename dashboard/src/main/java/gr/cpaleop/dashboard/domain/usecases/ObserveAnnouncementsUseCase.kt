package gr.cpaleop.dashboard.domain.usecases

import androidx.paging.PagingData
import gr.cpaleop.core.domain.entities.Announcement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface ObserveAnnouncementsUseCase {

    suspend operator fun invoke(coroutineScope: CoroutineScope): Flow<PagingData<Announcement>>

    /*suspend fun filter(filter: String)

    suspend fun sort(announcementsSort: AnnouncementSort)*/
}