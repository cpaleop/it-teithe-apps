package gr.cpaleop.teithe_apps.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.presentation.Message

abstract class BaseViewModel : ViewModel() {

    protected val _message = MutableLiveData<Message>()
    val message: LiveData<Message> = _message.toSingleEvent()
}