package gr.cpaleop.core.dispatchers

import kotlinx.coroutines.Dispatchers

/**
 * Denotes that this dispatcher corresponds to [Dispatchers.Default]
 */
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher