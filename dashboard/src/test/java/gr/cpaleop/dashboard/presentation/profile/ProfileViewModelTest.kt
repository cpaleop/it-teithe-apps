package gr.cpaleop.dashboard.presentation.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.common_test.LiveDataTest
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.domain.entities.*
import gr.cpaleop.dashboard.domain.usecases.GetProfileUseCase
import gr.cpaleop.dashboard.domain.usecases.UpdateSocialUseCase
import gr.cpaleop.dashboard.presentation.profile.options.ProfileOption
import gr.cpaleop.dashboard.presentation.profile.options.SelectedSocialOption
import gr.cpaleop.dashboard.presentation.profile.options.SelectedSocialOptionMapper
import gr.cpaleop.teithe_apps.di.dispatchers.MainDispatcher
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MainDispatcher
    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var getProfileUseCase: GetProfileUseCase

    @MockK
    private lateinit var profilePresentationMapper: ProfilePresentationMapper

    @MockK
    private lateinit var updateSocialUseCase: UpdateSocialUseCase

    @MockK
    private lateinit var selectedSocialOptionMapper: SelectedSocialOptionMapper

    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        viewModel = ProfileViewModel(
            testCoroutineDispatcher,
            getProfileUseCase,
            profilePresentationMapper,
            updateSocialUseCase,
            selectedSocialOptionMapper
        )
    }

    @Test
    fun `presentProfile success`() {
        val expected = profilePresentation
        coEvery { getProfileUseCase() } returns profile
        coEvery { profilePresentationMapper(profile) } returns profilePresentation
        viewModel.presentProfile()
        assertThat(LiveDataTest.getValue(viewModel.profile)).isEqualTo(expected)
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
    }

    @Test
    fun `presentProfile catches exception`() {
        coEvery { getProfileUseCase() } throws Throwable("")
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
                R.drawable.ic_edit
            )
        )
        viewModel.presentSocialOptions()
        assertThat(LiveDataTest.getValue(viewModel.socialOptions)).isEqualTo(expected)
    }

    @Test
    fun `handleOptionChoice correct values when option is edit`() = runBlocking {
        val givenChoice = "Edit"
        val givenValue = "Facebook"
        val expectedProfile = profilePresentation
        val expected = SelectedSocialOption(Social.FACEBOOK, "Facebook", "facebook")

        coEvery { getProfileUseCase() } returns profile
        coEvery { profilePresentationMapper(profile) } returns profilePresentation
        coEvery {
            selectedSocialOptionMapper(
                ProfileSocialDetails(
                    label = "Facebook",
                    socialLogoResource = R.drawable.ic_facebook,
                    content = "facebook",
                    socialType = Social.FACEBOOK
                )
            )
        } returns expected
        viewModel.presentProfile()
        assertThat(LiveDataTest.getValue(viewModel.profile)).isEqualTo(expectedProfile)
        viewModel.handleOptionChoice(givenChoice, givenValue)
        assertThat(LiveDataTest.getValue(viewModel.choiceEdit)).isEqualTo(expected)
    }

    @Test
    fun `handleOptionChoice correct values when option is copy`() = runBlocking {
        val givenChoice = "Copy"
        val givenValue = "Facebook"
        val expectedProfile = profilePresentation
        val expected = SelectedSocialOption(Social.FACEBOOK, "Facebook", "facebook")

        coEvery { getProfileUseCase() } returns profile
        coEvery { profilePresentationMapper(profile) } returns profilePresentation
        coEvery {
            selectedSocialOptionMapper(
                ProfileSocialDetails(
                    label = "Facebook",
                    socialLogoResource = R.drawable.ic_facebook,
                    content = "facebook",
                    socialType = Social.FACEBOOK
                )
            )
        } returns expected
        viewModel.presentProfile()
        assertThat(LiveDataTest.getValue(viewModel.profile)).isEqualTo(expectedProfile)
        viewModel.handleOptionChoice(givenChoice, givenValue)
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
    fun `updateSocial catches exception`() {
        val givenSocial = Social.FACEBOOK
        val givenValue = "facebook"
        coEvery { updateSocialUseCase(givenSocial, givenValue) } throws Throwable("")
        viewModel.updateSocial(givenSocial, givenValue)
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
    }

    companion object {

        private val profileAcademicDetails = ProfileAcademicDetails(
            am = "am",
            type = "student",
            displayName = "display_name",
            registeredYear = "2014",
            username = "username",
            currentSemester = "12"
        )

        private val profilePersonalDetails = ProfilePersonalDetails(
            lastName = "last_name",
            profileImageUrl = "image_url",
            websiteUrl = "website_url",
            telephoneNumber = "telephone_number",
            givenName = "given_name",
            description = "description"
        )

        private val profileSocialMedia = SocialMedia(
            facebook = "facebook",
            googlePlus = "google_plus",
            linkedIn = "linkedIn",
            twitter = "twitter",
            github = "github"
        )

        private val profile = Profile(
            email = "email@domain.com",
            academicDetails = profileAcademicDetails,
            personalDetails = profilePersonalDetails,
            socialMedia = profileSocialMedia
        )

        private val profileSocialDetails = listOf(
            ProfileSocialDetails(
                socialType = Social.GOOGLEPLUS,
                content = "google_plus",
                socialLogoResource = R.drawable.ic_google_plus,
                label = "Google+"
            ),
            ProfileSocialDetails(
                socialType = Social.FACEBOOK,
                content = "facebook",
                socialLogoResource = R.drawable.ic_facebook,
                label = "Facebook"
            ),
            ProfileSocialDetails(
                socialType = Social.TWITTER,
                content = "twitter",
                socialLogoResource = R.drawable.ic_twitter,
                label = "Twitter"
            ),
            ProfileSocialDetails(
                socialType = Social.LINKEDIN,
                content = "linkedIn",
                socialLogoResource = R.drawable.ic_linkedin,
                label = "LinkedIn"
            ),
            ProfileSocialDetails(
                socialType = Social.GITHUB,
                content = "github",
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
            email = "email@domain.com",
            social = profileSocialDetails,
            am = "am"
        )
    }
}