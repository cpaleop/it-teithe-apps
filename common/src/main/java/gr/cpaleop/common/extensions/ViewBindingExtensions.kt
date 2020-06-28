package gr.cpaleop.common.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import gr.cpaleop.common.ViewBindingObserver

fun <T : ViewBinding> T.setLifecycleOwner(lifecycleOwner: LifecycleOwner): T {
    lifecycleOwner.lifecycle.addObserver(ViewBindingObserver(this))
    return this
}