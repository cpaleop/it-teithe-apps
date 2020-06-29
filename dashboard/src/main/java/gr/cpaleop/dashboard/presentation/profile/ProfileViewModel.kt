package gr.cpaleop.dashboard.presentation.profile

import androidx.lifecycle.*
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.dashboard.domain.usecases.GetProfileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val profilePresentationMapper: ProfilePresentationMapper
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    private val _profile = MutableLiveData<ProfilePresentation>()
    private val profile: LiveData<ProfilePresentation> = _profile.toSingleEvent()

    val profilePictureUrl = MediatorLiveData<String>().apply {
        addSource(profile) {
            this.value = it.profilePhotoUrl
        }
    }

    val profileDetails = MediatorLiveData<MutableList<ProfilePresentationDetails>>().apply {
        addSource(profile) {
            viewModelScope.launch {
                this@apply.value = withContext(Dispatchers.Default) {
                    mutableListOf<ProfilePresentationDetails>().apply {
                        addAll(it.academicDetails)
                        addAll(it.personalDetails)
                        addAll(it.social)
                    }
                }
            }
        }
    }

    fun presentProfile() {
        viewModelScope.launch {
            try {
                delay(4000)
                _loading.value = true
                _profile.value = profilePresentationMapper(getProfileUseCase())
            } catch (t: Throwable) {
                Timber.e(t)
            } finally {
                _loading.value = false
            }
        }
    }
}