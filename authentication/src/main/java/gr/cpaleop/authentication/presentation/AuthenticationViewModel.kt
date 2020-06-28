package gr.cpaleop.authentication.presentation

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.cpaleop.authentication.BuildConfig
import gr.cpaleop.authentication.domain.usecases.BuildUriUseCase
import gr.cpaleop.authentication.domain.usecases.RetrieveTokenUseCase
import gr.cpaleop.common.extensions.toSingleEvent
import kotlinx.coroutines.launch
import timber.log.Timber

class AuthenticationViewModel(
    private val retrieveTokenUseCase: RetrieveTokenUseCase,
    private val buildUriUseCase: BuildUriUseCase
) : ViewModel() {

    private val _uri = MutableLiveData<Uri>()
    val uri: LiveData<Uri> = _uri.toSingleEvent()

    private val _tokenRetrieved = MutableLiveData<Unit>()
    val tokenRetrieved: LiveData<Unit> = _tokenRetrieved.toSingleEvent()

    fun presentUri() {
        viewModelScope.launch {
            _uri.value = Uri.parse(
                buildUriUseCase(
                    loginUrl = BuildConfig.LOGIN_URL,
                    clientId = BuildConfig.CLIENT_ID
                )
            )
        }
    }

    fun retrieveToken(code: String?) {
        viewModelScope.launch {
            try {
                _tokenRetrieved.value = retrieveTokenUseCase(code)
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }
}