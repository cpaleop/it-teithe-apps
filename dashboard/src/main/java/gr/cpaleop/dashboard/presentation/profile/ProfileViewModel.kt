package gr.cpaleop.dashboard.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.dashboard.domain.entities.Profile
import gr.cpaleop.dashboard.domain.usecases.GetProfileUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

class ProfileViewModel(private val getProfileUseCase: GetProfileUseCase) : ViewModel() {

    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> = _profile.toSingleEvent()

    fun presentProfile() {
        viewModelScope.launch {
            try {
                _profile.value = getProfileUseCase()
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }
}