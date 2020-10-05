package gr.cpaleop.profile.presentation

import androidx.appcompat.app.AppCompatDelegate
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.common_test.LiveDataTest
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.profile.R
import gr.cpaleop.profile.domain.entities.*
import gr.cpaleop.profile.domain.usecases.*
import gr.cpaleop.profile.presentation.options.*
import gr.cpaleop.profile.presentation.settings.Setting
import gr.cpaleop.profile.presentation.settings.SettingType
import gr.cpaleop.profile.presentation.settings.ThemeMapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    @DefaultDispatcher
    private val testDefaultCoroutineDispatcher = TestCoroutineDispatcher()

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
    private lateinit var optionDataMapper: OptionDataMapper

    @MockK
    private lateinit var getPreferredThemeUseCase: GetPreferredThemeUseCase

    @MockK
    private lateinit var updatePreferredThemeUseCase: UpdatePreferredThemeUseCase

    @MockK
    private lateinit var themeMapper: ThemeMapper

    @MockK
    private lateinit var logoutUseCase: LogoutUseCase

    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        viewModel = ProfileViewModel(
            testMainCoroutineDispatcher,
            testDefaultCoroutineDispatcher,
            getProfileUseCase,
            profilePresentationMapper,
            updateSocialUseCase,
            updatePersonalDetailsUseCase,
            selectedSocialOptionMapper,
            optionDataMapper,
            getPreferredThemeUseCase,
            updatePreferredThemeUseCase,
            themeMapper,
            logoutUseCase
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
    fun `presentProfile catches exception when throws`() {
        coEvery { getProfileUseCase() } throws Throwable()
        viewModel.presentProfile()
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
    }

    @Test
    fun `presentSocialOptions success`() {
        val expected = listOf(
            ProfileOption(
                "Copy",
                R.drawable.ic_copy
            ),
            ProfileOption(
                "Edit",
                appR.drawable.ic_edit
            )
        )
        viewModel.presentSocialOptions()
        assertThat(LiveDataTest.getValue(viewModel.socialOptions)).isEqualTo(expected)
    }

    @Test
    fun `handleOptionChoiceSocial correct values when option is edit`() = runBlocking {
        val givenChoice = "Edit"
        val givenValue = "Facebook"
        val expectedProfile = profilePresentation
        val expected = SelectedSocialOption(
            Social.FACEBOOK,
            "Facebook",
            "facebook"
        )

        coEvery { getProfileUseCase() } returns profile
        coEvery { profilePresentationMapper(profile) } returns profilePresentation
        coEvery {
            selectedSocialOptionMapper(
                ProfileSocialDetails(
                    label = "Facebook",
                    socialLogoResource = R.drawable.ic_facebook,
                    value = "facebook",
                    socialType = Social.FACEBOOK
                )
            )
        } returns expected
        viewModel.presentProfile()
        assertThat(LiveDataTest.getValue(viewModel.profile)).isEqualTo(expectedProfile)
        viewModel.handleOptionChoiceSocial(givenChoice, givenValue)
        assertThat(LiveDataTest.getValue(viewModel.choiceEditSocial)).isEqualTo(expected)
    }

    @Test
    fun `handleOptionChoiceSocial correct values when option is copy`() = runBlocking {
        val givenChoice = "Copy"
        val givenValue = "Facebook"
        val expectedProfile = profilePresentation
        val expected = OptionData(
            "Facebook",
            "facebook"
        )

        coEvery { getProfileUseCase() } returns profile
        coEvery { profilePresentationMapper(profile) } returns profilePresentation
        coEvery {
            optionDataMapper(
                ProfileSocialDetails(
                    label = "Facebook",
                    socialLogoResource = R.drawable.ic_facebook,
                    value = "facebook",
                    socialType = Social.FACEBOOK
                )
            )
        } returns expected
        viewModel.presentProfile()
        assertThat(LiveDataTest.getValue(viewModel.profile)).isEqualTo(expectedProfile)
        viewModel.handleOptionChoiceSocial(givenChoice, givenValue)
        assertThat(LiveDataTest.getValue(viewModel.choiceCopyToClipboard)).isEqualTo(expected)
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
    fun `updateSocial catches exception when throws`() {
        val givenSocial = Social.FACEBOOK
        val givenValue = "facebook"
        coEvery { updateSocialUseCase(givenSocial, givenValue) } throws Throwable()
        viewModel.updateSocial(givenSocial, givenValue)
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
    }

    @Test
    fun `presentSettings success when preferred theme is dark`() {
        val expected = settingsListDarkTheme
        val givenTheme = AppCompatDelegate.MODE_NIGHT_YES
        coEvery { getPreferredThemeUseCase() } returns givenTheme
        every { themeMapper(givenTheme) } returns "Dark"
        viewModel.presentSettings()
        assertThat(LiveDataTest.getValue(viewModel.settings)).isEqualTo(expected)
    }

    @Test
    fun `presentSettings success when preferred theme is light`() {
        val expected = settingsListLightTheme
        val givenTheme = AppCompatDelegate.MODE_NIGHT_NO
        coEvery { getPreferredThemeUseCase() } returns AppCompatDelegate.MODE_NIGHT_NO
        every { themeMapper(givenTheme) } returns "Light"
        viewModel.presentSettings()
        assertThat(LiveDataTest.getValue(viewModel.settings)).isEqualTo(expected)
    }

    @Test
    fun `presentSettings success when preferred theme is following system`() {
        val expected = settingsListSystemTheme
        val givenTheme = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        coEvery { getPreferredThemeUseCase() } returns givenTheme
        every { themeMapper(givenTheme) } returns "Follow system"
        viewModel.presentSettings()
        assertThat(LiveDataTest.getValue(viewModel.settings)).isEqualTo(expected)
    }

    @Test
    fun `presentPreferredTheme success when preferred theme is dark`() {
        val expected = AppCompatDelegate.MODE_NIGHT_YES
        coEvery { getPreferredThemeUseCase() } returns AppCompatDelegate.MODE_NIGHT_YES
        viewModel.presentPreferredTheme()
        assertThat(LiveDataTest.getValue(viewModel.preferredTheme)).isEqualTo(expected)
    }

    @Test
    fun `presentPreferredTheme success when preferred theme is light`() {
        val expected = AppCompatDelegate.MODE_NIGHT_NO
        coEvery { getPreferredThemeUseCase() } returns AppCompatDelegate.MODE_NIGHT_NO
        viewModel.presentPreferredTheme()
        assertThat(LiveDataTest.getValue(viewModel.preferredTheme)).isEqualTo(expected)
    }

    @Test
    fun `presentPreferredTheme success when preferred theme is following system`() {
        val expected = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        coEvery { getPreferredThemeUseCase() } returns AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        viewModel.presentPreferredTheme()
        assertThat(LiveDataTest.getValue(viewModel.preferredTheme)).isEqualTo(expected)
    }

    @Test
    fun `presentPreferredTheme catches exception when throws`() {
        coEvery { getPreferredThemeUseCase() } throws Throwable()
        viewModel.presentPreferredTheme()
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
    fun `updatePreferredTheme catches exception when throws`() {
        val givenTheme = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        coEvery { updatePreferredThemeUseCase(givenTheme) } throws Throwable()
        viewModel.updatePreferredTheme(givenTheme)
    }

    @Test
    fun `logout success`() {
        coEvery { logoutUseCase() } returns Unit
        viewModel.logout()
        assertThat(LiveDataTest.getValue(viewModel.logoutSuccess)).isEqualTo(Unit)
    }

    @Test
    fun `logout catches exception when throws`() {
        coEvery { logoutUseCase() } throws Throwable()
        viewModel.logout()
    }

    @Test
    fun `updatePersonal success`() {
        val givenPersonalLabel = "label"
        val givenPersonalValue = "value"
        val expected = profilePresentation
        coEvery { getProfileUseCase() } returns profile
        coEvery { profilePresentationMapper(profile) } returns profilePresentation
        viewModel.presentProfile()
        viewModel.updatePersonal(givenPersonalLabel, givenPersonalValue)
        assertThat(LiveDataTest.getValue(viewModel.profile)).isEqualTo(expected)
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
    }

    @Test
    fun `updatePersonal catches exception when throws`() {
        val givenPersonalLabel = "label"
        val givenPersonalValue = "value"
        coEvery { getProfileUseCase() } throws Throwable()
        coEvery { profilePresentationMapper(profile) } returns profilePresentation
        viewModel.presentProfile()
        viewModel.updatePersonal(givenPersonalLabel, givenPersonalValue)
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
    }

    @Test
    fun `handleOptionChoicePersonal success when choice is edit`() = runBlocking {
        val givenChoice = "Edit"
        val givenValue = "Mail"
        val expectedProfile = profilePresentation
        val expected = OptionData(
            "Mail",
            "email@domain.com"
        )

        coEvery { getProfileUseCase() } returns profile
        coEvery { profilePresentationMapper(profile) } returns profilePresentation
        coEvery {
            optionDataMapper(
                ProfilePersonalDetails(
                    type = Personal.MAIL,
                    label = "Mail",
                    value = "email@domain.com"
                )
            )
        } returns expected
        viewModel.presentProfile()
        assertThat(LiveDataTest.getValue(viewModel.profile)).isEqualTo(expectedProfile)
        viewModel.handleOptionChoicePersonal(givenChoice, givenValue)
        assertThat(LiveDataTest.getValue(viewModel.choiceEditPersonal)).isEqualTo(expected)
    }

    @Test
    fun `handleOptionChoicePersonal success when choice is copy`() = runBlocking {
        val givenChoice = "Copy"
        val givenValue = "Mail"
        val expectedProfile = profilePresentation
        val expected = OptionData(
            "Mail",
            "email@domain.com"
        )

        coEvery { getProfileUseCase() } returns profile
        coEvery { profilePresentationMapper(profile) } returns profilePresentation
        coEvery {
            optionDataMapper(
                ProfilePersonalDetails(
                    type = Personal.MAIL,
                    label = "Mail",
                    value = "email@domain.com"
                )
            )
        } returns expected
        viewModel.presentProfile()
        assertThat(LiveDataTest.getValue(viewModel.profile)).isEqualTo(expectedProfile)
        viewModel.handleOptionChoicePersonal(givenChoice, givenValue)
        assertThat(LiveDataTest.getValue(viewModel.choiceCopyToClipboard)).isEqualTo(expected)
    }

    companion object {

        private val settingsListSystemTheme = listOf(
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
                value = "Follow system"
            )
        )

        private val settingsListLightTheme = listOf(
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
                value = "Light"
            )
        )

        private val settingsListDarkTheme = listOf(
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
                value = "Dark"
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
                label = "Google+"
            ),
            ProfileSocialDetails(
                socialType = Social.FACEBOOK,
                value = "facebook",
                socialLogoResource = R.drawable.ic_facebook,
                label = "Facebook"
            ),
            ProfileSocialDetails(
                socialType = Social.TWITTER,
                value = "twitter",
                socialLogoResource = R.drawable.ic_twitter,
                label = "Twitter"
            ),
            ProfileSocialDetails(
                socialType = Social.LINKEDIN,
                value = "linkedIn",
                socialLogoResource = R.drawable.ic_linkedin,
                label = "LinkedIn"
            ),
            ProfileSocialDetails(
                socialType = Social.GITHUB,
                value = "github",
                socialLogoResource = R.drawable.ic_github,
                label = "Github"
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
                    label = "Name",
                    value = profile.academicDetails.displayName
                ),
                ProfilePersonalDetails(
                    type = Personal.TELEPHONE_NUMBER,
                    label = "Telephone number",
                    value = profile.personalDetails.telephoneNumber
                ),
                ProfilePersonalDetails(
                    type = Personal.MAIL,
                    label = "Mail",
                    value = profile.personalDetails.email
                ),
                ProfilePersonalDetails(
                    type = Personal.DESCRIPTION,
                    label = "Description",
                    value = profile.personalDetails.description
                )
            )
        )
    }
}