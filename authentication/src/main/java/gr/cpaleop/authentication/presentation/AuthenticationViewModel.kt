package gr.cpaleop.authentication.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import gr.cpaleop.authentication.domain.usecases.BuildUriUseCase
import gr.cpaleop.authentication.domain.usecases.RetrieveTokenUseCase
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.teithe_apps.presentation.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber

class AuthenticationViewModel(
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher,
    private val retrieveTokenUseCase: RetrieveTokenUseCase,
    private val buildUriUseCase: BuildUriUseCase
) : BaseViewModel() {

    private val _uri = MutableLiveData<String>()
    val uri: LiveData<String> = _uri.toSingleEvent()

    private val _tokenRetrieved = MutableLiveData<Unit>()
    val tokenRetrieved: LiveData<Unit> = _tokenRetrieved.toSingleEvent()

    fun presentUri() {
        viewModelScope.launch(mainDispatcher) {
            _uri.value = buildUriUseCase()
        }
    }

    fun retrieveToken(code: String?) {
        viewModelScope.launch(mainDispatcher) {
            try {
                _tokenRetrieved.value = retrieveTokenUseCase(code)
            } catch (t: Throwable) {
                Timber.e(t)
                handleNoConnectionException(t)
            }
        }
    }
}