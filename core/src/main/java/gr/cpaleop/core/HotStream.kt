package gr.cpaleop.core

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

@FlowPreview
@ExperimentalCoroutinesApi
open class HotStream<T> {

    private val channel = BroadcastChannel<T>(1)

    fun emit(data: T) {
        channel.offer(data)
    }

    fun asFlow(): Flow<T> {
        return channel.asFlow()
    }
}