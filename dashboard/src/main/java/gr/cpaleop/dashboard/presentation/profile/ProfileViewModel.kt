package gr.cpaleop.dashboard.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.domain.entities.Social
import gr.cpaleop.dashboard.domain.usecases.GetProfileUseCase
import gr.cpaleop.dashboard.domain.usecases.UpdateSocialUseCase
import gr.cpaleop.dashboard.presentation.profile.options.ProfileOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val profilePresentationMapper: ProfilePresentationMapper,
    private val updateSocialUseCase: UpdateSocialUseCase
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    private val _profile = MutableLiveData<ProfilePresentation>()
    val profile: LiveData<ProfilePresentation> = _profile.toSingleEvent()

    private val _socialOptions = MutableLiveData<List<ProfileOption>>()
    val socialOptions: LiveData<List<ProfileOption>> = _socialOptions.toSingleEvent()

    private val _choiceCopyToClipboard = MutableLiveData<SelectedSocialOption>()
    val choiceCopyToClipboard: LiveData<SelectedSocialOption> =
        _choiceCopyToClipboard.toSingleEvent()

    private val _choiceEdit = MutableLiveData<SelectedSocialOption>()
    val choiceEdit: LiveData<SelectedSocialOption> = _choiceEdit.toSingleEvent()

    fun presentProfile() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _profile.value = profilePresentationMapper(getProfileUseCase())
            } catch (t: Throwable) {
                Timber.e(t)
            } finally {
                _loading.value = false
            }
        }
    }

    fun presentSocialOptions() {
        viewModelScope.launch {
            _socialOptions.value = withContext(Dispatchers.Default) {
                listOf(
                    ProfileOption(
                        "Copy",
                        R.drawable.ic_copy
                    ),

                    ProfileOption(
                        "Edit",
                        R.drawable.ic_edit
                    )
                )
            }
        }
    }

    fun handleOptionChoice(choice: String, value: String) {
        viewModelScope.launch {
            val selectedContent =
                _profile.value?.social?.find { it.label == value }?.content ?: return@launch
            val selectedContentTitle =
                _profile.value?.social?.find { it.label == value }?.label ?: return@launch
            val social =
                _profile.value?.social?.find { it.label == value }?.socialType ?: return@launch
            val selectedSocialOption =
                SelectedSocialOption(social, selectedContentTitle, selectedContent)

            when (choice) {
                "Copy" -> _choiceCopyToClipboard.value = selectedSocialOption
                "Edit" -> _choiceEdit.value = selectedSocialOption
            }
        }
    }

    fun updateSocial(social: Social, value: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _profile.value = profilePresentationMapper(updateSocialUseCase(social, value))
            } catch (t: Throwable) {
                Timber.e(t)
            } finally {
                _loading.value = false
            }
        }
    }
}