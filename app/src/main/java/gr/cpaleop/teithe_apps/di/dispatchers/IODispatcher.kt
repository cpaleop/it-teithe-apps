package gr.cpaleop.teithe_apps.di.dispatchers

import kotlinx.coroutines.Dispatchers

/**
 * Denotes that this dispatcher corresponds to [Dispatchers.IO]
 */
@Retention(AnnotationRetention.BINARY)
annotation class IODispatcher