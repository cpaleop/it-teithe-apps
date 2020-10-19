package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.Notification
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@ExperimentalCoroutinesApi
interface ObserveNotificationsUseCase {

    val filterStream: StateFlow<String>

    suspend operator fun invoke(): Flow<List<Notification>>

    fun filter(filterQuery: String)
}