package gr.cpaleop.announcement.domain.usecases

import kotlinx.coroutines.flow.Flow

interface ObserveDownloadNotifierUseCase {

    suspend operator fun invoke(announcementId: String): Flow<Boolean>
}