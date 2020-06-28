package gr.cpaleop.common.extensions

import androidx.lifecycle.LiveData
import gr.cpaleop.common.LiveEvent

fun <T> LiveData<T>.toSingleEvent(): LiveData<T> {
    val result = LiveEvent<T>()
    result.addSource(this) {
        result.value = it
    }
    return result
}