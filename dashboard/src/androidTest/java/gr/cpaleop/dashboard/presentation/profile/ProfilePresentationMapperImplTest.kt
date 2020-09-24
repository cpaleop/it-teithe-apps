package gr.cpaleop.dashboard.presentation.profile

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.domain.entities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class ProfilePresentationMapperImplTest {

    private val applicationContext = InstrumentationRegistry.getInstrumentation().targetContext

    @DefaultDispatcher
    private val defaultDispatcher = Dispatchers.Default

    private lateinit var profilePresentationMapper: ProfilePresentationMapper

    @Before
    fun setup() {
        profilePresentationMapper =
            ProfilePresentationMapperImpl(applicationContext, defaultDispatcher)
    }

    @Test
    fun invoke_succeeds() = runBlocking {
        val given = profile
        val expected = profilePresentation
        val actual = profilePresentationMapper(given)
        assertThat(actual).isEqualTo(expected)
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
                socialType = Social.FACEBOOK,
                content = "facebook",
                socialLogoResource = R.drawable.ic_facebook,
                label = "Facebook"
            ),
            ProfileSocialDetails(
                socialType = Social.GITHUB,
                content = "github",
                socialLogoResource = R.drawable.ic_github,
                label = "Github"
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
                socialType = Social.GOOGLEPLUS,
                content = "google_plus",
                socialLogoResource = R.drawable.ic_google_plus,
                label = "Google+"
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