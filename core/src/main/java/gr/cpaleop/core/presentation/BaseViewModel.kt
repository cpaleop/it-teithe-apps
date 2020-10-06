package gr.cpaleop.core.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gr.cpaleop.common.extensions.toSingleEvent

abstract class BaseViewModel : ViewModel() {

    protected val _message = MutableLiveData<SnackbarMessage>()
    val message: LiveData<SnackbarMessage> = _message.toSingleEvent()
}