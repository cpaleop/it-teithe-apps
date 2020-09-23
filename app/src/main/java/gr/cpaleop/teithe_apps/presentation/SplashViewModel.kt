package gr.cpaleop.teithe_apps.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.teithe_apps.di.dispatchers.MainDispatcher
import gr.cpaleop.teithe_apps.domain.usecases.AuthenticatedUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber

class SplashViewModel(
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher,
    private val authenticatedUseCase: AuthenticatedUseCase
) : ViewModel() {

    private val _isUserLoggedIn = MutableLiveData<Boolean>()
    val isUserLoggedIn: LiveData<Boolean> = _isUserLoggedIn.toSingleEvent()

    fun checkUserAuthentication() {
        viewModelScope.launch(mainDispatcher) {
            try {
                _isUserLoggedIn.value = authenticatedUseCase()
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }
}