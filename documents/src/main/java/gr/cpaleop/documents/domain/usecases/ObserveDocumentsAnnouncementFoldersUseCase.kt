package gr.cpaleop.documents.domain.usecases

import gr.cpaleop.documents.domain.entities.AnnouncementFolder
import kotlinx.coroutines.flow.Flow

interface ObserveDocumentsAnnouncementFoldersUseCase {

    suspend operator fun invoke(): Flow<List<AnnouncementFolder>>

    suspend fun filter(filterQuery: String)
}