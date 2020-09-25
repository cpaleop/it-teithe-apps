package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.AnnouncementFolder
import kotlinx.coroutines.flow.Flow

interface ObserveDocumentsAnnouncementFoldersUseCase {

    suspend operator fun invoke(): Flow<List<AnnouncementFolder>>

    suspend fun filter(filterQuery: String)
}