package gr.cpaleop.profile.domain.usecases

import kotlinx.coroutines.flow.Flow

interface ObservePreferredThemeUseCase {

    operator fun invoke(): Flow<Int>
}