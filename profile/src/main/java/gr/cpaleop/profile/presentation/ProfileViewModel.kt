package gr.cpaleop.profile.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.profile.R
import gr.cpaleop.profile.domain.entities.Social
import gr.cpaleop.profile.domain.usecases.GetProfileUseCase
import gr.cpaleop.profile.domain.usecases.UpdateSocialUseCase
import gr.cpaleop.profile.presentation.options.ProfileOption
import gr.cpaleop.profile.presentation.options.SelectedSocialOption
import gr.cpaleop.profile.presentation.options.SelectedSocialOptionMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber
import gr.cpaleop.teithe_apps.R as appR

class ProfileViewModel(
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher,
    private val getProfileUseCase: GetProfileUseCase,
    private val profilePresentationMapper: ProfilePresentationMapper,
    private val updateSocialUseCase: UpdateSocialUseCase,
    private val selectedSocialOptionMapper: SelectedSocialOptionMapper
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
        viewModelScope.launch(mainDispatcher) {
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
        viewModelScope.launch(mainDispatcher) {
            _socialOptions.value = listOf(
                ProfileOption(
                    "Copy",
                    R.drawable.ic_copy
                ),
                ProfileOption(
                    "Edit",
                    appR.drawable.ic_edit
                )
            )
        }
    }

    fun handleOptionChoice(choice: String, value: String) {
        viewModelScope.launch(mainDispatcher) {
            val selectedProfileSocialDetails =
                _profile.value?.social?.find { it.label == value } ?: return@launch
            val selectedSocialOption = selectedSocialOptionMapper(selectedProfileSocialDetails)

            when (choice) {
                "Copy" -> _choiceCopyToClipboard.value = selectedSocialOption
                "Edit" -> _choiceEdit.value = selectedSocialOption
            }
        }
    }

    fun updateSocial(social: Social, value: String) {
        viewModelScope.launch(mainDispatcher) {
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