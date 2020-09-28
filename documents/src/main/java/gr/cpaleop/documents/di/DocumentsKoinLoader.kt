package gr.cpaleop.documents.di

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

/**
 * Convenient singleton task to load koin modules.
 * There's an issue with loading and unloding withing a fragment when we reuse the same fragment.
 */
@ExperimentalCoroutinesApi
@FlowPreview
object DocumentsKoinLoader {

    private var counter: Int = 0

    private val isLocked: Boolean
        get() = counter == 0

    fun load() {
        if (isLocked) {
            loadKoinModules(documentsModule)
            increment()
        }
    }

    fun unload() {
        if (isLocked) {
            unloadKoinModules(documentsModule)
            decrement()
        }
    }

    private fun increment() {
        counter++
    }

    private fun decrement() {
        counter--
    }
}