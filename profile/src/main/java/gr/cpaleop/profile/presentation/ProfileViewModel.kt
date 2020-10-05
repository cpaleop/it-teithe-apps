package gr.cpaleop.profile.presentation

import androidx.lifecycle.*
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.profile.R
import gr.cpaleop.profile.domain.entities.Social
import gr.cpaleop.profile.domain.usecases.*
import gr.cpaleop.profile.presentation.options.*
import gr.cpaleop.profile.presentation.settings.Setting
import gr.cpaleop.profile.presentation.settings.SettingType
import gr.cpaleop.profile.presentation.settings.ThemeMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private val optionDataMapper: OptionDataMapper,
    private val getPreferredThemeUseCase: GetPreferredThemeUseCase,
    private val updatePreferredThemeUseCase: UpdatePreferredThemeUseCase,
    private val themeMapper: ThemeMapper,
    private val logoutUseCase: LogoutUseCase
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

    private val _settings = MutableLiveData<List<Setting>>()
    val settings: LiveData<List<Setting>> = _settings.toSingleEvent()

    private val _preferredTheme = MutableLiveData<Int>()
    val preferredTheme: LiveData<Int> = _preferredTheme.toSingleEvent()

    private val _updatedTheme = MutableLiveData<Int>()
    val updatedTheme: LiveData<Int> = _updatedTheme.toSingleEvent()

    private val _logoutSuccess = MutableLiveData<Unit>()
    val logoutSuccess: LiveData<Unit> = _logoutSuccess.toSingleEvent()

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

    fun presentSettings() {
        viewModelScope.launch(mainDispatcher) {
            try {
                _settings.value = withContext(Dispatchers.Default) {
                    listOf(
                        Setting(
                            type = SettingType.SECTION_TITLE,
                            title = "Account"
                        ),
                        Setting(
                            type = SettingType.CONTENT,
                            iconRes = R.drawable.ic_key,
                            title = "Change password"
                        ),
                        Setting(
                            type = SettingType.CONTENT,
                            iconRes = R.drawable.ic_logout,
                            title = "Logout"
                        ),
                        Setting(
                            type = SettingType.SECTION_TITLE,
                            title = "Appearance"
                        ),
                        Setting(
                            type = SettingType.CONTENT,
                            iconRes = R.drawable.ic_theme,
                            title = "Change theme",
                            value = themeMapper(getPreferredThemeUseCase())
                        )
                    )
                }
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun presentPreferredTheme() {
        viewModelScope.launch(mainDispatcher) {
            try {
                _preferredTheme.value = getPreferredThemeUseCase()
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun updatePreferredTheme(theme: Int) {
        viewModelScope.launch(mainDispatcher) {
            try {
                updatePreferredThemeUseCase(theme)
                _updatedTheme.value = theme
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun logout() {
        viewModelScope.launch(mainDispatcher) {
            try {
                _logoutSuccess.value = logoutUseCase()
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun handleOptionChoiceSocial(choice: String, value: String) {
        viewModelScope.launch(mainDispatcher) {
            val selectedProfileSocialDetails =
                _profile.value?.social?.find { it.label == value } ?: return@launch

            when (choice) {
                "Copy" -> _choiceCopyToClipboard.value =
                    optionDataMapper(selectedProfileSocialDetails)
                "Edit" -> _choiceEditSocial.value =
                    selectedSocialOptionMapper(selectedProfileSocialDetails)
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