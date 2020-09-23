package gr.cpaleop.teithe_apps.di.dispatchers

import kotlinx.coroutines.Dispatchers

/**
 * Denotes that this dispatcher corresponds to [Dispatchers.Main]
 */
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher