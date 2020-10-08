package gr.cpaleop.teithe_apps.presentation.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import gr.cpaleop.core.presentation.Message
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass

abstract class BaseApiActivity<VB : ViewBinding, VM : BaseViewModel>(viewModelClass: KClass<VM>) :
    BaseActivity<VB>() {

    protected val viewModel: VM by viewModel<VM>(viewModelClass)

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeError()
    }

    private fun observeError() {
        viewModel.run {
            val lifecycleOwner = this@BaseApiActivity
            message.observe(lifecycleOwner, Observer(::showSnackbarMessage))
        }
    }

    private fun showSnackbarMessage(message: Message) {
        Snackbar.make(
            binding.root,
            getString(message.resource, message.arguments),
            Snackbar.LENGTH_LONG
        ).show()
    }
}