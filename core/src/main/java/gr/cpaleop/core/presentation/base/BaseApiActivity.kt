package gr.cpaleop.core.presentation.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import gr.cpaleop.core.R
import gr.cpaleop.core.presentation.SnackbarMessage
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
            noConnection.observe(lifecycleOwner, { showNoConnectionMessage() })
            message.observe(lifecycleOwner, Observer(::showSnackbarMessage))
        }
    }

    private fun showNoConnectionMessage() {
        val message = SnackbarMessage(R.string.error_no_internet_connection)
        showSnackbarMessage(message)
    }

    private fun showSnackbarMessage(snackbarMessage: SnackbarMessage) {
        Snackbar.make(binding.root, snackbarMessage.resource, Snackbar.LENGTH_LONG).show()
    }
}