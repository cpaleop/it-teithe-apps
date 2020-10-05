package gr.cpaleop.profile.presentation

import androidx.lifecycle.*
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.profile.R
import gr.cpaleop.profile.domain.entities.Social
import gr.cpaleop.profile.domain.usecases.GetProfileUseCase
import gr.cpaleop.profile.domain.usecases.UpdatePersonalDetailsUseCase
import gr.cpaleop.profile.domain.usecases.UpdateSocialUseCase
import gr.cpaleop.profile.presentation.options.*
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
    private val updatePersonalDetailsUseCase: UpdatePersonalDetailsUseCase,
    private val selectedSocialOptionMapper: SelectedSocialOptionMapper,
    private val optionDataMapper: OptionDataMapper
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    private val _profile = MutableLiveData<ProfilePresentation>()
    val profile: LiveData<ProfilePresentation> = _profile.toSingleEvent()

    val profileSocials: MediatorLiveData<List<ProfileSocialDetails>> by lazy {
        MediatorLiveData<List<ProfileSocialDetails>>().apply {
            addSource(profile) {
                this.value = it.social
            }
        }
    }

    val profilePersonalDetails: MediatorLiveData<List<ProfilePersonalDetails>> by lazy {
        MediatorLiveData<List<ProfilePersonalDetails>>().apply {
            addSource(profile) {
                this.value = it.personalDetails
            }
        }
    }

    private val _socialOptions = MutableLiveData<List<ProfileOption>>()
    val socialOptions: LiveData<List<ProfileOption>> = _socialOptions.toSingleEvent()

    private val _choiceCopyToClipboard = MutableLiveData<OptionData>()
    val choiceCopyToClipboard: LiveData<OptionData> =
        _choiceCopyToClipboard.toSingleEvent()

    private val _choiceEditSocial = MutableLiveData<SelectedSocialOption>()
    val choiceEditSocial: LiveData<SelectedSocialOption> = _choiceEditSocial.toSingleEvent()

    private val _choiceEditPersonal = MutableLiveData<OptionData>()
    val choiceEditPersonal: LiveData<OptionData> = _choiceEditPersonal.toSingleEvent()

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

    fun handleOptionChoiceSocial(choice: String, value: String) {
        viewModelScope.launch(mainDispatcher) {
            val selectedProfileSocialDetails =
                _profile.value?.social?.find { it.label == value } ?: return@launch
            val selectedSocialOption = selectedSocialOptionMapper(selectedProfileSocialDetails)

            when (choice) {
                "Copy" -> _choiceCopyToClipboard.value =
                    optionDataMapper(selectedProfileSocialDetails)
                "Edit" -> _choiceEditSocial.value = selectedSocialOption
            }
        }
    }

    fun handleOptionChoicePersonal(choice: String, value: String) {
        viewModelScope.launch(mainDispatcher) {
            val selectedProfileSocialDetails =
                _profile.value?.personalDetails?.find { it.label == value } ?: return@launch

            when (choice) {
                "Copy" -> _choiceCopyToClipboard.value =
                    optionDataMapper(selectedProfileSocialDetails)
                "Edit" -> _choiceEditPersonal.value = optionDataMapper(selectedProfileSocialDetails)
            }
        }
    }

    fun updatePersonal(personalLabel: String, value: String) {
        viewModelScope.launch(mainDispatcher) {
            try {
                _loading.value = true
                val personalType =
                    profile.value?.personalDetails?.find { it.label == personalLabel }?.type
                        ?: return@launch
                _profile.value =
                    profilePresentationMapper(updatePersonalDetailsUseCase(personalType, value))
            } catch (t: Throwable) {
                Timber.e(t)
            } finally {
                _loading.value = false
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