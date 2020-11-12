package gr.cpaleop.common.extensions

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/**
 * Extension function for Iterable
 *
 * Accepts a [suspend] block and applies it on every element
 *
 * [block] is executed in [async] coroutine under [coroutineScope] and utilizes [awaitAll] for
 * gathering the mapped list
 */
suspend inline fun <T, R> Iterable<T>.mapAsync(crossinline block: (T) -> R): List<R> =
    coroutineScope {
        map { async { block(it) } }.awaitAll()
    }

/**
 * Extension function for Iterable
 *
 * Accepts a [suspend] block and applies it on every element
 *
 * [block] is a suspendable function that gets executed in [async] coroutine under [coroutineScope] and utilizes [awaitAll] for
 * gathering the mapped list
 */
suspend inline fun <T, R> Iterable<T>.mapAsyncSuspended(crossinline block: suspend (T) -> R): List<R> =
    coroutineScope {
        map { async { block(it) } }.awaitAll()
    }

/**
 * Returns a list containing all the elements that exist in the subject list but not in the [another] list
 *
 * @param another list to search if elements from the list exist
 * @return a list of elements that do not exist in subject list
 */
fun <T> List<T>.diff(another: List<T>): List<T> {
    return this.filter { !another.contains(it) }
}