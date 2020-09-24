package gr.cpaleop.core

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

@FlowPreview
@ExperimentalCoroutinesApi
open class ColdStream<T> {

    private val channel = ConflatedBroadcastChannel<T>()

    fun emit(data: T) {
        channel.offer(data)
    }

    fun asFlow(): Flow<T> {
        return channel.asFlow()
    }
}