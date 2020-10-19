package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.Notification
import gr.cpaleop.network.connection.NoConnectionException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@ExperimentalCoroutinesApi
interface ObserveNotificationsUseCase {

    val filterStream: StateFlow<String>

    @Throws(Throwable::class, NoConnectionException::class)
    suspend operator fun invoke(): Flow<List<Notification>>

    fun filter(filterQuery: String)
}