package gr.cpaleop.core.dispatchers

import kotlinx.coroutines.Dispatchers

/**
 * Denotes that this dispatcher corresponds to [Dispatchers.IO]
 */
@Retention(AnnotationRetention.BINARY)
annotation class IODispatcher