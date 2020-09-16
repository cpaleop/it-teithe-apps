package gr.cpaleop.dashboard.presentation.profile

import android.content.Context
import gr.cpaleop.common.extensions.safe
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.domain.entities.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfilePresentationMapper(private val applicationContext: Context) {

    suspend operator fun invoke(profile: Profile): ProfilePresentation =
        withContext(Dispatchers.Default) {
            val socialsList = listOf(
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_socials_facebook_title),
                    content = profile.socialMedia.facebook.safe("-")
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_socials_github_title),
                    content = profile.socialMedia.github.safe("-")
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_socials_twitter_title),
                    content = profile.socialMedia.twitter.safe("-")
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_socials_linkedin_title),
                    content = profile.socialMedia.linkedIn.safe("-")
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_socials_googleplus_title),
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