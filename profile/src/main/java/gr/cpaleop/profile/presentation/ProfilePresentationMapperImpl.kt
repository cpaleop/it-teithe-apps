package gr.cpaleop.profile.presentation

import gr.cpaleop.common.extensions.orEmpty
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.profile.R
import gr.cpaleop.profile.domain.entities.Personal
import gr.cpaleop.profile.domain.entities.Profile
import gr.cpaleop.profile.domain.entities.Social
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ProfilePresentationMapperImpl(@DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher) :
    ProfilePresentationMapper {

    override suspend operator fun invoke(profile: Profile): ProfilePresentation =
        withContext(defaultDispatcher) {
            val socialsList = listOf(
                ProfileSocialDetails(
                    socialType = Social.FACEBOOK,
                    labelRes = R.string.profile_socials_facebook_title,
                    socialLogoResource = R.drawable.ic_facebook,
                    value = profile.socialMedia.facebook.orEmpty("-")
                ),
                ProfileSocialDetails(
                    socialType = Social.GITHUB,
                    labelRes = R.string.profile_socials_github_title,
                    socialLogoResource = R.drawable.ic_github,
                    value = profile.socialMedia.github.orEmpty("-")
                ),
                ProfileSocialDetails(
                    socialType = Social.TWITTER,
                    labelRes = R.string.profile_socials_twitter_title,
                    socialLogoResource = R.drawable.ic_twitter,
                    value = profile.socialMedia.twitter.orEmpty("-")
                ),
                ProfileSocialDetails(
                    socialType = Social.LINKEDIN,
                    labelRes = R.string.profile_socials_linkedin_title,
                    socialLogoResource = R.drawable.ic_linkedin,
                    value = profile.socialMedia.linkedIn.orEmpty("-")
                ),
                ProfileSocialDetails(
                    socialType = Social.GOOGLEPLUS,
                    socialLogoResource = R.drawable.ic_google_plus,
                    labelRes = R.string.profile_socials_googleplus_title,
                    value = profile.socialMedia.googlePlus.orEmpty("-")
                )
            )

            val personalDetails = listOf(
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

            return@withContext ProfilePresentation(
                profilePhotoUrl = profile.personalDetails.profileImageUrl,
                am = profile.academicDetails.am,
                username = profile.academicDetails.username,
                displayName = profile.personalDetails.givenName + " " + profile.personalDetails.lastName,
                semester = profile.academicDetails.currentSemester,
                registeredYear = profile.academicDetails.registeredYear,
                social = socialsList,
                personalDetails = personalDetails
            )
        }
}