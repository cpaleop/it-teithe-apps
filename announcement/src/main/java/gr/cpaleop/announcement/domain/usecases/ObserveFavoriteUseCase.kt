package gr.cpaleop.announcement.domain.usecases

import kotlinx.coroutines.flow.Flow

interface ObserveFavoriteUseCase {

    operator fun invoke(announcementId: String): Flow<Boolean>
}