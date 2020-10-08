package gr.cpaleop.profile.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.domain.behavior.LanguageCode
import gr.cpaleop.core.presentation.Message
import gr.cpaleop.network.connection.NoConnectionException
import gr.cpaleop.profile.R
import gr.cpaleop.profile.domain.entities.Personal
import gr.cpaleop.profile.domain.entities.Social
import gr.cpaleop.profile.domain.usecases.*
import gr.cpaleop.profile.presentation.options.*
import gr.cpaleop.profile.presentation.personal.PersonalOptionData
import gr.cpaleop.profile.presentation.personal.PersonalOptionDataMapper
import gr.cpaleop.profile.presentation.settings.LanguageMapper
import gr.cpaleop.profile.presentation.settings.Setting
import gr.cpaleop.profile.presentation.settings.SettingType
import gr.cpaleop.profile.presentation.settings.theme.ThemeMapper
import gr.cpaleop.teithe_apps.presentation.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
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
    private val optionDataMapper: OptionDataMapper,
    private val personalOptionDataMapper: PersonalOptionDataMapper,
    private val observePreferredThemeUseCase: ObservePreferredThemeUseCase,
    private val updatePreferredThemeUseCase: UpdatePreferredThemeUseCase,
    private val themeMapper: ThemeMapper,
    private val logoutUseCase: LogoutUseCase,
    private val getPreferredLanguageUseCase: GetPreferredLanguageUseCase,
    private val updatePreferredLanguageUseCase: UpdatePreferredLanguageUseCase,
    private val languageMapper: LanguageMapper
) : BaseViewModel() {

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

    private val _choiceEditPersonal = MutableLiveData<PersonalOptionData>()
    val choiceEditPersonal: LiveData<PersonalOptionData> = _choiceEditPersonal.toSingleEvent()

    private val _selectedLanguage = MutableLiveData<@LanguageCode String>()
    val selectedLanguage: LiveData<String> = _selectedLanguage.toSingleEvent()

    private val _updatedLanguage = MutableLiveData<Unit>()
    val updatedLanguage: LiveData<Unit> = _updatedLanguage.toSingleEvent()

    fun presentProfile() {
        viewModelScope.launch(mainDispatcher) {
            try {
                _loading.value = true
                _profile.value = profilePresentationMapper(getProfileUseCase())
            } catch (t: NoConnectionException) {
                Timber.e(t)
                _message.value = Message(appR.string.error_no_internet_connection)
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(appR.string.error_generic)
            } finally {
                _loading.value = false
            }
        }
    }

    fun presentSocialOptions() {
        viewModelScope.launch(mainDispatcher) {
            try {
                _socialOptions.value = listOf(
                    ProfileOption(
                        R.string.profile_option_copy,
                        R.drawable.ic_copy
                    ),
                    ProfileOption(
                        R.string.profile_option_edit,
                        appR.drawable.ic_edit
                    )
                )
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(appR.string.error_generic)
            }
        }
    }

    fun presentSettings() {
        viewModelScope.launch(mainDispatcher) {
            try {
                observePreferredThemeUseCase()
                    .collect { theme ->
                        val selectedLanguage = languageMapper(getPreferredLanguageUseCase())
                        _settings.value = listOf(
                            Setting(
                                type = SettingType.SECTION_TITLE,
                                titleRes = R.string.profile_settings_account_title
                            ),
                            Setting(
                                type = SettingType.CONTENT,
                                iconRes = R.drawable.ic_key,
                                titleRes = R.string.profile_settings_change_password
                            ),
                            Setting(
                                type = SettingType.CONTENT,
                                iconRes = R.drawable.ic_logout,
                                titleRes = R.string.profile_settings_logout
                            ),
                            Setting(
                                type = SettingType.SECTION_TITLE,
                                titleRes = R.string.profile_settings_appearance_title
                            ),
                            Setting(
                                type = SettingType.CONTENT,
                                iconRes = R.drawable.ic_theme,
                                titleRes = R.string.profile_settings_change_theme,
                                valueRes = themeMapper(theme)
                            ),
                            Setting(
                                type = SettingType.CONTENT,
                                iconRes = R.drawable.ic_language,
                                titleRes = R.string.profile_settings_change_language,
                                valueRes = selectedLanguage
                            )
                        )
                    }
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(appR.string.error_generic)
            }
        }
    }

    fun presentPreferredTheme() {
        viewModelScope.launch(mainDispatcher) {
            try {
                observePreferredThemeUseCase()
                    .collect(_preferredTheme::setValue)
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(appR.string.error_generic)
            }
        }
    }

    fun presentPreferredLanguage() {
        viewModelScope.launch(mainDispatcher) {
            try {
                _selectedLanguage.value = getPreferredLanguageUseCase()
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(appR.string.error_generic)
            }
        }
    }

    fun updatePreferredLanguage(@LanguageCode language: String) {
        viewModelScope.launch(mainDispatcher) {
            try {
                _updatedLanguage.value = updatePreferredLanguageUseCase(language)
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(appR.string.error_generic)
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
                _message.value = Message(appR.string.error_generic)
            }
        }
    }

    fun logout() {
        viewModelScope.launch(mainDispatcher) {
            try {
                _logoutSuccess.value = logoutUseCase()
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(appR.string.error_generic)
            }
        }
    }

    fun handleOptionChoiceSocial(@StringRes choice: Int, type: Social) {
        viewModelScope.launch(mainDispatcher) {
            try {
                val selectedProfileSocialDetails =
                    _profile.value?.social?.find { it.socialType == type } ?: return@launch

                when (choice) {
                    R.string.profile_option_copy -> _choiceCopyToClipboard.value =
                        optionDataMapper(selectedProfileSocialDetails)
                    R.string.profile_option_edit -> _choiceEditSocial.value =
                        selectedSocialOptionMapper(selectedProfileSocialDetails)
                }
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(appR.string.error_generic)
            }
        }
    }

    fun handleOptionChoicePersonal(@StringRes choice: Int, type: Personal) {
        viewModelScope.launch(mainDispatcher) {
            try {
                val selectedPersonalDetails =
                    _profile.value?.personalDetails?.find { it.type == type } ?: return@launch

                when (choice) {
                    R.string.profile_option_copy -> _choiceCopyToClipboard.value =
                        optionDataMapper(selectedPersonalDetails)
                    R.string.profile_option_edit -> _choiceEditPersonal.value =
                        personalOptionDataMapper(selectedPersonalDetails)
                }
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun updatePersonal(type: Personal, value: String) {
        viewModelScope.launch(mainDispatcher) {
            try {
                _loading.value = true
                _profile.value =
                    profilePresentationMapper(updatePersonalDetailsUseCase(type, value))
            } catch (t: NoConnectionException) {
                Timber.e(t)
                _message.value = Message(appR.string.error_no_internet_connection)
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(appR.string.error_generic)
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
            } catch (t: NoConnectionException) {
                Timber.e(t)
                _message.value = Message(appR.string.error_no_internet_connection)
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(appR.string.error_generic)
            } finally {
                _loading.value = false
            }
        }
    }
}