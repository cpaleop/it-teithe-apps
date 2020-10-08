package gr.cpaleop.documents.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Singleton cold channel for filtering since AnnouncementFolder list and Document list use the same source of filter
 */
@ExperimentalCoroutinesApi
class FilterStream {

    private val _channel = MutableStateFlow("")
    private val channel: StateFlow<String> = _channel

    var value: String
        get() {
            return _channel.value
        }
        set(value) {
            _channel.value = value
        }

    fun asFlow(): Flow<String> = channel
}