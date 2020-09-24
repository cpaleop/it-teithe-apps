package gr.cpaleop.dashboard.presentation.profile

import android.content.Context
import gr.cpaleop.common.extensions.safe
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.domain.entities.Profile
import gr.cpaleop.dashboard.domain.entities.Social
import gr.cpaleop.teithe_apps.di.dispatchers.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ProfilePresentationMapperImpl(
    private val applicationContext: Context,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher
) : ProfilePresentationMapper {

    override suspend operator fun invoke(profile: Profile): ProfilePresentation =
        withContext(defaultDispatcher) {
            val socialsList = listOf(
                ProfileSocialDetails(
                    socialType = Social.FACEBOOK,
                    label = applicationContext.getString(R.string.profile_socials_facebook_title),
                    socialLogoResource = R.drawable.ic_facebook,
                    content = profile.socialMedia.facebook.safe("-")
                ),
                ProfileSocialDetails(
                    socialType = Social.GITHUB,
                    label = applicationContext.getString(R.string.profile_socials_github_title),
                    socialLogoResource = R.drawable.ic_github,
                    content = profile.socialMedia.github.safe("-")
                ),
                ProfileSocialDetails(
                    socialType = Social.TWITTER,
                    label = applicationContext.getString(R.string.profile_socials_twitter_title),
                    socialLogoResource = R.drawable.ic_twitter,
                    content = profile.socialMedia.twitter.safe("-")
                ),
                ProfileSocialDetails(
                    socialType = Social.LINKEDIN,
                    label = applicationContext.getString(R.string.profile_socials_linkedin_title),
                    socialLogoResource = R.drawable.ic_linkedin,
                    content = profile.socialMedia.linkedIn.safe("-")
                ),
                ProfileSocialDetails(
                    socialType = Social.GOOGLEPLUS,
                    socialLogoResource = R.drawable.ic_google_plus,
                    label = applicationContext.getString(R.string.profile_socials_googleplus_title),
                    content = profile.socialMedia.googlePlus.safe("-")
                )
            )

            return@withContext ProfilePresentation(
                profilePhotoUrl = profile.personalDetails.profileImageUrl,
                am = profile.academicDetails.am,
                email = profile.email,
                username = profile.academicDetails.username,
                displayName = profile.academicDetails.displayName,
                semester = profile.academicDetails.currentSemester,
                registeredYear = profile.academicDetails.registeredYear,
                social = socialsList
            )
        }
}