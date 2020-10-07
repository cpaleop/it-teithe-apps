package gr.cpaleop.teithe_apps.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.connection.NoConnectionException
import gr.cpaleop.core.presentation.SnackbarMessage
import gr.cpaleop.teithe_apps.R as appR

abstract class BaseViewModel : ViewModel() {

    protected val _message = MutableLiveData<SnackbarMessage>()
    val message: LiveData<SnackbarMessage> = _message.toSingleEvent()

    protected fun handleNoConnectionException(throwable: Throwable) {
        _message.value = if (throwable is NoConnectionException) {
            SnackbarMessage(appR.string.error_no_internet_connection)
        } else {
            SnackbarMessage(appR.string.error_generic)
        }
    }
}