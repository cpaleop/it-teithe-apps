package gr.cpaleop.teithe_apps.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.presentation.Message
import gr.cpaleop.teithe_apps.domain.usecases.AuthenticatedUseCase
import gr.cpaleop.teithe_apps.presentation.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber
import gr.cpaleop.teithe_apps.R as appR

class SplashViewModel(
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher,
    private val authenticatedUseCase: AuthenticatedUseCase
) : BaseViewModel() {

    private val _isUserLoggedIn = MutableLiveData<Boolean>()
    val isUserLoggedIn: LiveData<Boolean> = _isUserLoggedIn.toSingleEvent()

    fun checkUserAuthentication() {
        viewModelScope.launch(mainDispatcher) {
            try {
                _isUserLoggedIn.value = authenticatedUseCase()
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(appR.string.error_generic)
            }
        }
    }
}