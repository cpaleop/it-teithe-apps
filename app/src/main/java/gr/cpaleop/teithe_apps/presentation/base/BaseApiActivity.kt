package gr.cpaleop.teithe_apps.presentation.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import gr.cpaleop.core.presentation.SnackbarMessage
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass

abstract class BaseApiActivity<VB : ViewBinding, VM : BaseViewModel>(viewModelClass: KClass<VM>) :
    gr.cpaleop.teithe_apps.presentation.base.BaseActivity<VB>() {

    protected val viewModel: VM by viewModel<VM>(viewModelClass)

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeError()
    }

    private fun observeError() {
        viewModel.message.observe(this, Observer(::showSnackbar))
    }

    private fun showSnackbar(snackbarMessage: SnackbarMessage) {
        Snackbar.make(binding.root, snackbarMessage.resource, Snackbar.LENGTH_LONG).show()
    }
}