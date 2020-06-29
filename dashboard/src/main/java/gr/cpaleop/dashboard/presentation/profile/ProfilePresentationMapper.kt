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
            val profilePersonalDetailsList = listOf(
                ProfilePresentationDetails(
                    type = ProfileDetailsType.TITLE,
                    title = applicationContext.getString(R.string.profile_personal_details_title),
                    content = ""
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_personal_lastname_title),
                    content = profile.personalDetails.lastName.safe("-")
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_personal_description_title),
                    content = profile.personalDetails.description.safe("-")
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_personal_givenName_title),
                    content = profile.personalDetails.givenName.safe("-")
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_personal_telephoneNumber_title),
                    content = profile.personalDetails.telephoneNumber.safe("-")
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_personal_website_title),
                    content = profile.personalDetails.websiteUrl.safe("-")
                )
            )

            val profileAcademidDetailsList = listOf(
                ProfilePresentationDetails(
                    type = ProfileDetailsType.TITLE,
                    title = applicationContext.getString(R.string.profile_academic_details_title),
                    content = ""
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_academic_am_title),
                    content = profile.academicDetails.am.safe("-")
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_academic_currentSemester_title),
                    content = profile.academicDetails.currentSemester.safe("-")
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_academic_displayName_title),
                    content = profile.academicDetails.displayName.safe("-")
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_academic_registeredYear_title),
                    content = profile.academicDetails.registeredYear.safe("-")
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_academic_type_title),
                    content = profile.academicDetails.type.safe("-")
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_academic_username_title),
                    content = profile.academicDetails.username.safe("-")
                )
            )

            val socialsList = listOf(
                ProfilePresentationDetails(
                    type = ProfileDetailsType.TITLE,
                    title = applicationContext.getString(R.string.profile_socials_title),
                    content = ""
                ),
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
                personalDetails = profilePersonalDetailsList,
                academicDetails = profileAcademidDetailsList,
                social = socialsList
            )
        }
}