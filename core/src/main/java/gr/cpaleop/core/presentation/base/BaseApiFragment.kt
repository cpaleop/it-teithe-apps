package gr.cpaleop.core.presentation.base

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import gr.cpaleop.core.R
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
        viewModel.run {
            noConnection.observe(viewLifecycleOwner, { showNoConnectionMessage() })
            message.observe(viewLifecycleOwner, Observer(::showSnackbarMessage))
        }
    }

    private fun showNoConnectionMessage() {
        val message = SnackbarMessage(R.string.error_no_internet_connection)
        showSnackbarMessage(message)
    }

    protected fun showSnackbarMessage(snackbarMessage: SnackbarMessage) {
        Snackbar.make(
            binding.root,
            getString(snackbarMessage.resource, *snackbarMessage.arguments),
            Snackbar.LENGTH_LONG
        ).show()
    }
}