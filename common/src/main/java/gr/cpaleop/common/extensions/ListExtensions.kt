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
 * Returns the [target] element matching the given [predicate], or `null` if element was not found.
 */
inline fun <T> Iterable<T>.getOrNull(target: Int, predicate: (T) -> Boolean): T? {
    var counter = 1
    for (element in this) {
        if (predicate(element) && counter == target) {
            return element
        } else if (predicate(element)) counter++
    }
    return null
}

/**
 * Returns a list containing first [n] elements. If list doesn't have [n] elements, then the returned list is filled with nulls.
 *
 * @throws IllegalArgumentException if [n] is negative.
 */
fun <T> List<T>.takeOrNulls(n: Int): List<T?> {
    require(n >= 0) { "Requested element count $n is less than zero." }
    val list = ArrayList<T?>(n)
    for (i in 0 until n) {
        list.add(this.getOrNull(i))
    }
    return list
}