package gr.cpaleop.core.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.connection.NoConnectionException
import gr.cpaleop.core.presentation.SnackbarMessage

abstract class BaseViewModel : ViewModel() {

    protected val _noConnection = MutableLiveData<Unit>()
    val noConnection: LiveData<Unit> = _noConnection.toSingleEvent()

    protected val _message = MutableLiveData<SnackbarMessage>()
    val message: LiveData<SnackbarMessage> = _message.toSingleEvent()

    protected fun handleNoConnectionException(throwable: Throwable) {
        if (throwable is NoConnectionException) {
            _noConnection.value = Unit
        }
    }
}