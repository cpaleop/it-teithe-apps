package gr.cpaleop.core.presentation

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
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
        viewModel.message.observe(this, Observer(::showSnackbar))
    }

    private fun showSnackbar(snackbarMessage: SnackbarMessage) {
        Snackbar.make(binding.root, snackbarMessage.message, Snackbar.LENGTH_LONG).show()
    }
}