package gr.cpaleop.profile.presentation

import androidx.appcompat.app.AppCompatDelegate
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.common_test.LiveDataTest
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.domain.behavior.LanguageCode
import gr.cpaleop.core.presentation.Message
import gr.cpaleop.network.connection.NoConnectionException
import gr.cpaleop.profile.R
import gr.cpaleop.profile.domain.entities.*
import gr.cpaleop.profile.domain.usecases.*
import gr.cpaleop.profile.presentation.options.*
import gr.cpaleop.profile.presentation.personal.PersonalOptionData
import gr.cpaleop.profile.presentation.personal.PersonalOptionDataMapper
import gr.cpaleop.profile.presentation.settings.LanguageMapper
import gr.cpaleop.profile.presentation.settings.Setting
import gr.cpaleop.profile.presentation.settings.SettingType
import gr.cpaleop.profile.presentation.settings.theme.ThemeMapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import gr.cpaleop.teithe_apps.R as appR

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MainDispatcher
    private val testMainCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var getProfileUseCase: GetProfileUseCase

    @MockK
    private lateinit var profilePresentationMapper: ProfilePresentationMapper

    @MockK
    private lateinit var updateSocialUseCase: UpdateSocialUseCase

    @MockK
    private lateinit var updatePersonalDetailsUseCase: UpdatePersonalDetailsUseCase

    @MockK
    private lateinit var selectedSocialOptionMapper: SelectedSocialOptionMapper

    @MockK
    private lateinit var personalOptionDataMapper: PersonalOptionDataMapper

    @MockK
    private lateinit var optionDataMapper: OptionDataMapper

    @MockK
    private lateinit var observePreferredThemeUseCase: ObservePreferredThemeUseCase

    @MockK
    private lateinit var updatePreferredThemeUseCase: UpdatePreferredThemeUseCase

    @MockK
    private lateinit var themeMapper: ThemeMapper

    @MockK
    private lateinit var logoutUseCase: LogoutUseCase

    @MockK
    private lateinit var getPreferredLanguageUseCase: GetPreferredLanguageUseCase

    @MockK
    private lateinit var updatePreferredLanguageUseCase: UpdatePreferredLanguageUseCase

    @MockK
    private lateinit var languageMapper: LanguageMapper

    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        viewModel = ProfileViewModel(
            testMainCoroutineDispatcher,
            getProfileUseCase,
            profilePresentationMapper,
            updateSocialUseCase,
            updatePersonalDetailsUseCase,
            selectedSocialOptionMapper,
            optionDataMapper,
            personalOptionDataMapper,
            observePreferredThemeUseCase,
            updatePreferredThemeUseCase,
            themeMapper,
            logoutUseCase,
            getPreferredLanguageUseCase,
            updatePreferredLanguageUseCase,
            languageMapper
        )
    }

    @Test
    fun `presentProfile success`() {
        val expected = profilePresentation
        coEvery { getProfileUseCase() } returns profile
        coEvery { profilePresentationMapper(profile) } returns profilePresentation
        viewModel.presentProfile()
        assertThat(LiveDataTest.getValue(viewModel.profile)).isEqualTo(expected)
        assertThat(LiveDataTest.getValue(viewModel.profileSocials)).isEqualTo(expected.social)
        assertThat(LiveDataTest.getValue(viewModel.profilePersonalDetails)).isEqualTo(expected.personalDetails)
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
    }

    @Test
    fun `presentProfile catches exception and has message when failure`() {
        val expectedMessage = Message(appR.string.error_generic)
        coEvery { getProfileUseCase() } throws Throwable()
        viewModel.presentProfile()
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    @Test
    fun `presentProfile catches exception and has message when no internet connection`() {
        val expectedMessage = Message(appR.string.error_no_internet_connection)
        coEvery { getProfileUseCase() } throws NoConnectionException()
        viewModel.presentProfile()
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    @Test
    fun `presentSocialOptions success`() {
        val expected = listOf(
            ProfileOption(
                R.string.profile_option_copy,
                R.drawable.ic_copy
            ),
            ProfileOption(
                R.string.profile_option_edit,
                appR.drawable.ic_edit
            )
        )
        viewModel.presentSocialOptions()
        assertThat(LiveDataTest.getValue(viewModel.socialOptions)).isEqualTo(expected)
    }

    @Test
    fun `presentSettings success when preferred theme is dark`() {
        val expected = settingsListDarkTheme
        val givenTheme = AppCompatDelegate.MODE_NIGHT_YES
        val givenThemeFlow = flow {
            emit(givenTheme)
        }
        every { observePreferredThemeUseCase() } returns givenThemeFlow
        coEvery { getPreferredLanguageUseCase() } returns LanguageCode.ENGLISH
        every { languageMapper(LanguageCode.ENGLISH) } returns R.string.profile_language_english
        every { themeMapper(givenTheme) } returns R.string.profile_theme_dark
        viewModel.presentSettings()
        assertThat(LiveDataTest.getValue(viewModel.settings)).isEqualTo(expected)
    }

    @Test
    fun `presentSettings success when preferred theme is light`() {
        val expected = settingsListLightTheme
        val givenTheme = AppCompatDelegate.MODE_NIGHT_NO
        val givenThemeFlow = flow {
            emit(givenTheme)
        }
        every { observePreferredThemeUseCase() } returns givenThemeFlow
        coEvery { getPreferredLanguageUseCase() } returns LanguageCode.ENGLISH
        every { languageMapper(LanguageCode.ENGLISH) } returns R.string.profile_language_english
        every { themeMapper(givenTheme) } returns R.string.profile_theme_light
        viewModel.presentSettings()
        assertThat(LiveDataTest.getValue(viewModel.settings)).isEqualTo(expected)
    }

    @Test
    fun `presentSettings success when preferred theme is following system`() {
        val expected = settingsListSystemTheme
        val givenTheme = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        val givenThemeFlow = flow {
            emit(givenTheme)
        }
        every { observePreferredThemeUseCase() } returns givenThemeFlow
        coEvery { getPreferredLanguageUseCase() } returns LanguageCode.ENGLISH
        every { languageMapper(LanguageCode.ENGLISH) } returns R.string.profile_language_english
        every { themeMapper(givenTheme) } returns R.string.profile_theme_system
        viewModel.presentSettings()
        assertThat(LiveDataTest.getValue(viewModel.settings)).isEqualTo(expected)
    }

    @Test
    fun `presentSettings success when preferred language is Greek`() {
        val expected = settingsListDarkThemeGreekLanguage
        val givenTheme = AppCompatDelegate.MODE_NIGHT_YES
        val givenThemeFlow = flow {
            emit(givenTheme)
        }
        every { observePreferredThemeUseCase() } returns givenThemeFlow
        coEvery { getPreferredLanguageUseCase() } returns LanguageCode.GREEK
        every { languageMapper(LanguageCode.GREEK) } returns R.string.profile_language_greek
        every { themeMapper(givenTheme) } returns R.string.profile_theme_dark
        viewModel.presentSettings()
        assertThat(LiveDataTest.getValue(viewModel.settings)).isEqualTo(expected)
    }

    @Test
    fun `presentSettings catches exception and has message when failure`() {
        val expectedMessage = Message(appR.string.error_generic)
        every { observePreferredThemeUseCase() } throws Throwable()
        viewModel.presentSettings()
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    @Test
    fun `presentPreferredTheme success when preferred theme is dark`() {
        val expected = AppCompatDelegate.MODE_NIGHT_YES
        val themeFlow = flow {
            emit(AppCompatDelegate.MODE_NIGHT_YES)
        }
        every { observePreferredThemeUseCase() } returns themeFlow
        viewModel.presentPreferredTheme()
        assertThat(LiveDataTest.getValue(viewModel.preferredTheme)).isEqualTo(expected)
    }

    @Test
    fun `presentPreferredTheme success when preferred theme is light`() {
        val expected = AppCompatDelegate.MODE_NIGHT_NO
        val themeFlow = flow {
            emit(AppCompatDelegate.MODE_NIGHT_NO)
        }
        every { observePreferredThemeUseCase() } returns themeFlow
        viewModel.presentPreferredTheme()
        assertThat(LiveDataTest.getValue(viewModel.preferredTheme)).isEqualTo(expected)
    }

    @Test
    fun `presentPreferredTheme success when preferred theme is following system`() {
        val expected = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        val themeFlow = flow {
            emit(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
        every { observePreferredThemeUseCase() } returns themeFlow
        viewModel.presentPreferredTheme()
        assertThat(LiveDataTest.getValue(viewModel.preferredTheme)).isEqualTo(expected)
    }

    @Test
    fun `presentPreferredTheme catches exception and has message when failure`() {
        val expectedMessage = Message(appR.string.error_generic)
        every { observePreferredThemeUseCase() } throws Throwable()
        viewModel.presentPreferredTheme()
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    @Test
    fun `updatePreferredTheme success when preferred theme is dark`() {
        val givenTheme = AppCompatDelegate.MODE_NIGHT_YES
        val expected = AppCompatDelegate.MODE_NIGHT_YES
        coEvery { updatePreferredThemeUseCase(givenTheme) } returns Unit
        viewModel.updatePreferredTheme(givenTheme)
        assertThat(LiveDataTest.getValue(viewModel.updatedTheme)).isEqualTo(expected)
    }

    @Test
    fun `updatePreferredTheme success when preferred theme is light`() {
        val givenTheme = AppCompatDelegate.MODE_NIGHT_NO
        val expected = AppCompatDelegate.MODE_NIGHT_NO
        coEvery { updatePreferredThemeUseCase(givenTheme) } returns Unit
        viewModel.updatePreferredTheme(givenTheme)
        assertThat(LiveDataTest.getValue(viewModel.updatedTheme)).isEqualTo(expected)
    }

    @Test
    fun `updatePreferredTheme success when preferred theme is following system`() {
        val givenTheme = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        val expected = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        coEvery { updatePreferredThemeUseCase(givenTheme) } returns Unit
        viewModel.updatePreferredTheme(givenTheme)
        assertThat(LiveDataTest.getValue(viewModel.updatedTheme)).isEqualTo(expected)
    }

    @Test
    fun `updatePreferredTheme catches exception and has message when failure`() {
        val expectedMessage = Message(appR.string.error_generic)
        val givenTheme = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        coEvery { updatePreferredThemeUseCase(givenTheme) } throws Throwable()
        viewModel.updatePreferredTheme(givenTheme)
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    @Test
    fun `presentPreferredLanguage presents Greek when preferred language is Greek`() = runBlocking {
        val expected = "el"
        coEvery { getPreferredLanguageUseCase() } returns LanguageCode.GREEK
        viewModel.presentPreferredLanguage()
        assertThat(LiveDataTest.getValue(viewModel.selectedLanguage)).isEqualTo(expected)
    }

    @Test
    fun `presentPreferredLanguage presents English (US) when preferred language is English (US)`() =
        runBlocking {
            val expected = "en"
            coEvery { getPreferredLanguageUseCase() } returns LanguageCode.ENGLISH
            viewModel.presentPreferredLanguage()
            assertThat(LiveDataTest.getValue(viewModel.selectedLanguage)).isEqualTo(expected)
        }

    @Test
    fun `presentPreferredLanguage catches exception and has message when failure`() = runBlocking {
        val expectedMessage = Message(appR.string.error_generic)
        coEvery { getPreferredLanguageUseCase() } throws Throwable()
        viewModel.presentPreferredLanguage()
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    @Test
    fun `updatePreferredLanguage presents Greek when preferred language is Greek`() = runBlocking {
        val givenLanguage = "el"
        val expected = Unit
        coEvery { updatePreferredLanguageUseCase(givenLanguage) } returns Unit
        viewModel.updatePreferredLanguage(givenLanguage)
        assertThat(LiveDataTest.getValue(viewModel.updatedLanguage)).isEqualTo(expected)
    }

    @Test
    fun `updatePreferredLanguage presents English (US) when preferred language is English (US)`() =
        runBlocking {
            val givenLanguage = "en"
            val expected = Unit
            coEvery { updatePreferredLanguageUseCase(givenLanguage) } returns Unit
            viewModel.updatePreferredLanguage(givenLanguage)
            assertThat(LiveDataTest.getValue(viewModel.updatedLanguage)).isEqualTo(expected)
        }

    @Test
    fun `updatePreferredLanguage catches exception when failure`() = runBlocking {
        val expectedMessage = Message(appR.string.error_generic)
        val givenLanguage = "en"
        coEvery { updatePreferredLanguageUseCase(givenLanguage) } throws Throwable()
        viewModel.updatePreferredLanguage(givenLanguage)
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    @Test
    fun `logout success`() {
        coEvery { logoutUseCase() } returns Unit
        viewModel.logout()
        assertThat(LiveDataTest.getValue(viewModel.logoutSuccess)).isEqualTo(Unit)
    }

    @Test
    fun `logout catches exception and has message when failure`() {
        val expectedMessage = Message(appR.string.error_generic)
        coEvery { logoutUseCase() } throws Throwable()
        viewModel.logout()
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    @Test
    fun `handleOptionChoiceSocial correct values when option is edit`() = runBlocking {
        val givenChoiceRes = R.string.profile_option_edit
        val givenSocialType = Social.FACEBOOK
        val expectedProfile = profilePresentation
        val expected = SelectedSocialOption(
            Social.FACEBOOK,
            R.string.profile_socials_facebook_title,
            "facebook"
        )

        coEvery { getProfileUseCase() } returns profile
        coEvery { profilePresentationMapper(profile) } returns profilePresentation
        coEvery {
            selectedSocialOptionMapper(
                ProfileSocialDetails(
                    labelRes = R.string.profile_socials_facebook_title,
                    socialLogoResource = R.drawable.ic_facebook,
                    value = "facebook",
                    socialType = Social.FACEBOOK
                )
            )
        } returns expected
        viewModel.presentProfile()
        assertThat(LiveDataTest.getValue(viewModel.profile)).isEqualTo(expectedProfile)
        viewModel.handleOptionChoiceSocial(givenChoiceRes, givenSocialType)
        assertThat(LiveDataTest.getValue(viewModel.choiceEditSocial)).isEqualTo(expected)
    }

    @Test
    fun `handleOptionChoiceSocial correct values when option is copy`() = runBlocking {
        val givenChoiceRes = R.string.profile_option_copy
        val givenSocialType = Social.FACEBOOK
        val expectedProfile = profilePresentation
        val expected = OptionData(
            R.string.profile_socials_facebook_title,
            "facebook"
        )

        coEvery { getProfileUseCase() } returns profile
        coEvery { profilePresentationMapper(profile) } returns profilePresentation
        coEvery {
            optionDataMapper(
                ProfileSocialDetails(
                    labelRes = R.string.profile_socials_facebook_title,
                    socialLogoResource = R.drawable.ic_facebook,
                    value = "facebook",
                    socialType = Social.FACEBOOK
                )
            )
        } returns expected
        viewModel.presentProfile()
        assertThat(LiveDataTest.getValue(viewModel.profile)).isEqualTo(expectedProfile)
        viewModel.handleOptionChoiceSocial(givenChoiceRes, givenSocialType)
        assertThat(LiveDataTest.getValue(viewModel.choiceCopyToClipboard)).isEqualTo(expected)
    }

    @Test
    fun `handleOptionChoicePersonal success when choice is copy`() = runBlocking {
        val givenChoiceRes = R.string.profile_option_copy
        val givenPersonalType = Personal.MAIL
        val expectedProfile = profilePresentation
        val expected = OptionData(
            R.string.profile_personal_mail,
            "email@domain.com"
        )

        coEvery { getProfileUseCase() } returns profile
        coEvery { profilePresentationMapper(profile) } returns profilePresentation
        coEvery {
            optionDataMapper(
                ProfilePersonalDetails(
                    type = Personal.MAIL,
                    label = R.string.profile_personal_mail,
                    value = "email@domain.com"
                )
            )
        } returns expected
        viewModel.presentProfile()
        assertThat(LiveDataTest.getValue(viewModel.profile)).isEqualTo(expectedProfile)
        viewModel.handleOptionChoicePersonal(givenChoiceRes, givenPersonalType)
        assertThat(LiveDataTest.getValue(viewModel.choiceCopyToClipboard)).isEqualTo(expected)
    }

    @Test
    fun `handleOptionChoicePersonal success when choice is edit`() = runBlocking {
        val givenChoice = R.string.profile_option_edit
        val givenPersonalType = Personal.MAIL
        val expectedProfile = profilePresentation
        val expected = PersonalOptionData(
            type = Personal.MAIL,
            labelRes = R.string.profile_personal_mail,
            value = "email@domain.com"
        )

        coEvery { getProfileUseCase() } returns profile
        coEvery { profilePresentationMapper(profile) } returns profilePresentation
        coEvery {
            personalOptionDataMapper(
                ProfilePersonalDetails(
                    type = Personal.MAIL,
                    label = R.string.profile_personal_mail,
                    value = "email@domain.com"
                )
            )
        } returns expected
        viewModel.presentProfile()
        assertThat(LiveDataTest.getValue(viewModel.profile)).isEqualTo(expectedProfile)
        viewModel.handleOptionChoicePersonal(givenChoice, givenPersonalType)
        assertThat(LiveDataTest.getValue(viewModel.choiceEditPersonal)).isEqualTo(expected)
    }

    @Test
    fun `updatePersonal success`() {
        val givenPersonalType = Personal.MAIL
        val givenPersonalValue = "value"
        val expected = profilePresentation
        coEvery {
            updatePersonalDetailsUseCase(
                givenPersonalType,
                givenPersonalValue
            )
        } returns profile
        coEvery { profilePresentationMapper(profile) } returns profilePresentation
        viewModel.updatePersonal(givenPersonalType, givenPersonalValue)
        assertThat(LiveDataTest.getValue(viewModel.profile)).isEqualTo(expected)
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
    }

    @Test
    fun `updatePersonal catches exception and has message when failure`() {
        val expectedMessage = Message(appR.string.error_generic)
        val givenPersonalType = Personal.MAIL
        val givenPersonalValue = "value"
        coEvery {
            updatePersonalDetailsUseCase(
                givenPersonalType,
                givenPersonalValue
            )
        } throws Throwable()
        coEvery { profilePresentationMapper(profile) } returns profilePresentation
        viewModel.updatePersonal(givenPersonalType, givenPersonalValue)
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    @Test
    fun `updatePersonal catches exception and has message when no internet connection`() {
        val expectedMessage = Message(appR.string.error_no_internet_connection)
        val givenPersonalType = Personal.MAIL
        val givenPersonalValue = "value"
        coEvery {
            updatePersonalDetailsUseCase(
                givenPersonalType,
                givenPersonalValue
            )
        } throws NoConnectionException()
        coEvery { profilePresentationMapper(profile) } returns profilePresentation
        viewModel.updatePersonal(givenPersonalType, givenPersonalValue)
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    @Test
    fun `updateSocial success`() {
        val givenSocial = Social.FACEBOOK
        val givenValue = "facebook"
        val expected = profilePresentation
        coEvery { updateSocialUseCase(givenSocial, givenValue) } returns profile
        coEvery { profilePresentationMapper(profile) } returns profilePresentation
        viewModel.updateSocial(givenSocial, givenValue)
        assertThat(LiveDataTest.getValue(viewModel.profile)).isEqualTo(expected)
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
    }

    @Test
    fun `updateSocial catches exception and has message when failure`() {
        val expectedMessage = Message(appR.string.error_generic)
        val givenSocial = Social.FACEBOOK
        val givenValue = "facebook"
        coEvery { updateSocialUseCase(givenSocial, givenValue) } throws Throwable()
        viewModel.updateSocial(givenSocial, givenValue)
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    @Test
    fun `updateSocial catches exception and has message when no internet exception`() {
        val expectedMessage = Message(appR.string.error_no_internet_connection)
        val givenSocial = Social.FACEBOOK
        val givenValue = "facebook"
        coEvery { updateSocialUseCase(givenSocial, givenValue) } throws NoConnectionException()
        viewModel.updateSocial(givenSocial, givenValue)
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    companion object {

        private val settingsListSystemTheme = listOf(
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
                valueRes = R.string.profile_theme_system
            ),
            Setting(
                type = SettingType.CONTENT,
                iconRes = R.drawable.ic_language,
                titleRes = R.string.profile_settings_change_language,
                valueRes = R.string.profile_language_english
            )
        )

        private val settingsListLightTheme = listOf(
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
                valueRes = R.string.profile_theme_light
            ),
            Setting(
                type = SettingType.CONTENT,
                iconRes = R.drawable.ic_language,
                titleRes = R.string.profile_settings_change_language,
                valueRes = R.string.profile_language_english
            )
        )

        private val settingsListDarkTheme = listOf(
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
                valueRes = R.string.profile_theme_dark
            ),
            Setting(
                type = SettingType.CONTENT,
                iconRes = R.drawable.ic_language,
                titleRes = R.string.profile_settings_change_language,
                valueRes = R.string.profile_language_english
            )
        )

        private val settingsListDarkThemeGreekLanguage = listOf(
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
                valueRes = R.string.profile_theme_dark
            ),
            Setting(
                type = SettingType.CONTENT,
                iconRes = R.drawable.ic_language,
                titleRes = R.string.profile_settings_change_language,
                valueRes = R.string.profile_language_greek
            )
        )

        private val profileAcademicDetails =
            AcademicDetails(
                am = "am",
                type = "student",
                displayName = "display_name",
                registeredYear = "2014",
                username = "username",
                currentSemester = "12"
            )

        private val profilePersonalDetails =
            PersonalDetails(
                lastName = "last_name",
                profileImageUrl = "image_url",
                websiteUrl = "website_url",
                telephoneNumber = "telephone_number",
                givenName = "given_name",
                description = "description",
                email = "email@domain.com"
            )

        private val profileSocialMedia = SocialMedia(
            facebook = "facebook",
            googlePlus = "google_plus",
            linkedIn = "linkedIn",
            twitter = "twitter",
            github = "github"
        )

        private val profile = Profile(
            academicDetails = profileAcademicDetails,
            personalDetails = profilePersonalDetails,
            socialMedia = profileSocialMedia
        )

        private val profileSocialDetails = listOf(
            ProfileSocialDetails(
                socialType = Social.GOOGLEPLUS,
                value = "google_plus",
                socialLogoResource = R.drawable.ic_google_plus,
                labelRes = R.string.profile_socials_googleplus_title
            ),
            ProfileSocialDetails(
                socialType = Social.FACEBOOK,
                value = "facebook",
                socialLogoResource = R.drawable.ic_facebook,
                labelRes = R.string.profile_socials_facebook_title
            ),
            ProfileSocialDetails(
                socialType = Social.TWITTER,
                value = "twitter",
                socialLogoResource = R.drawable.ic_twitter,
                labelRes = R.string.profile_socials_twitter_title
            ),
            ProfileSocialDetails(
                socialType = Social.LINKEDIN,
                value = "linkedIn",
                socialLogoResource = R.drawable.ic_linkedin,
                labelRes = R.string.profile_socials_linkedin_title
            ),
            ProfileSocialDetails(
                socialType = Social.GITHUB,
                value = "github",
                socialLogoResource = R.drawable.ic_github,
                labelRes = R.string.profile_socials_github_title
            )
        )

        private val profilePresentation = ProfilePresentation(
            profilePhotoUrl = "image_url",
            username = "username",
            registeredYear = "2014",
            displayName = "display_name",
            semester = "12",
            social = profileSocialDetails,
            am = "am",
            personalDetails = listOf(
                ProfilePersonalDetails(
                    type = Personal.DISPLAY_NAME,
                    label = R.string.profile_personal_name,
                    value = profile.academicDetails.displayName
                ),
                ProfilePersonalDetails(
                    type = Personal.TELEPHONE_NUMBER,
                    label = R.string.profile_personal_telephone,
                    value = profile.personalDetails.telephoneNumber
                ),
                ProfilePersonalDetails(
                    type = Personal.MAIL,
                    label = R.string.profile_personal_mail,
                    value = profile.personalDetails.email
                ),
                ProfilePersonalDetails(
                    type = Personal.DESCRIPTION,
                    label = R.string.profile_personal_description,
                    value = profile.personalDetails.description
                )
            )
        )
    }
}