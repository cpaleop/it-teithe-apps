package gr.cpaleop.teithe_apps.presentation.base

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import gr.cpaleop.core.presentation.SnackbarMessage
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import kotlin.reflect.KClass

abstract class BaseApiFragment<VB : ViewBinding, VM : BaseViewModel>(viewModelClass: KClass<VM>) :
    BaseFragment<VB>() {

    protected val viewModel: VM by sharedViewModel<VM>(viewModelClass)

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeError()
    }

    private fun observeError() {
        viewModel.message.observe(viewLifecycleOwner, Observer(::showSnackbarMessage))
    }

    protected fun showSnackbarMessage(snackbarMessage: SnackbarMessage) {
        Snackbar.make(
            binding.root,
            getString(snackbarMessage.resource, *snackbarMessage.arguments),
            Snackbar.LENGTH_LONG
        ).show()
    }
}