package gr.cpaleop.dashboard.presentation.profile

import android.content.Context
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
                    content = profile.personalDetails.lastName
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_personal_description_title),
                    content = profile.personalDetails.description
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_personal_givenName_title),
                    content = profile.personalDetails.givenName
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_personal_telephoneNumber_title),
                    content = profile.personalDetails.telephoneNumber
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_personal_website_title),
                    content = profile.personalDetails.websiteUrl
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
                    content = profile.academicDetails.am
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_academic_currentSemester_title),
                    content = profile.academicDetails.currentSemester
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_academic_displayName_title),
                    content = profile.academicDetails.displayName
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_academic_registeredYear_title),
                    content = profile.academicDetails.registeredYear
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_academic_type_title),
                    content = profile.academicDetails.type
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_academic_username_title),
                    content = profile.academicDetails.username
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
                    content = profile.socialMedia.facebook
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_socials_github_title),
                    content = profile.socialMedia.github
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_socials_twitter_title),
                    content = profile.socialMedia.twitter
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_socials_linkedin_title),
                    content = profile.socialMedia.linkedIn
                ),
                ProfilePresentationDetails(
                    type = ProfileDetailsType.CONTENT,
                    title = applicationContext.getString(R.string.profile_socials_googleplus_title),
                    content = profile.socialMedia.googlePlus
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