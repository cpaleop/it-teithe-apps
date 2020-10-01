package gr.cpaleop.profile.presentation

import android.content.Context
import gr.cpaleop.common.extensions.orEmpty
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.profile.R
import gr.cpaleop.profile.domain.entities.Profile
import gr.cpaleop.profile.domain.entities.Social
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
                    content = profile.socialMedia.facebook.orEmpty("-")
                ),
                ProfileSocialDetails(
                    socialType = Social.GITHUB,
                    label = applicationContext.getString(R.string.profile_socials_github_title),
                    socialLogoResource = R.drawable.ic_github,
                    content = profile.socialMedia.github.orEmpty("-")
                ),
                ProfileSocialDetails(
                    socialType = Social.TWITTER,
                    label = applicationContext.getString(R.string.profile_socials_twitter_title),
                    socialLogoResource = R.drawable.ic_twitter,
                    content = profile.socialMedia.twitter.orEmpty("-")
                ),
                ProfileSocialDetails(
                    socialType = Social.LINKEDIN,
                    label = applicationContext.getString(R.string.profile_socials_linkedin_title),
                    socialLogoResource = R.drawable.ic_linkedin,
                    content = profile.socialMedia.linkedIn.orEmpty("-")
                ),
                ProfileSocialDetails(
                    socialType = Social.GOOGLEPLUS,
                    socialLogoResource = R.drawable.ic_google_plus,
                    label = applicationContext.getString(R.string.profile_socials_googleplus_title),
                    content = profile.socialMedia.googlePlus.orEmpty("-")
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