package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.core.domain.entities.Document
import kotlinx.coroutines.flow.Flow

interface ObserveDocumentsUseCase {

    suspend operator fun invoke(announcementId: String?): Flow<List<Document>>

    suspend fun filter(filterQuery: String)
}