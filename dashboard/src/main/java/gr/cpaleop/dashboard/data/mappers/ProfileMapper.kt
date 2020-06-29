package gr.cpaleop.dashboard.data.mappers

import gr.cpaleop.common.extensions.safe
import gr.cpaleop.core.domain.behavior.LanguageCode
import gr.cpaleop.dashboard.data.model.remote.RemoteProfile
import gr.cpaleop.dashboard.domain.entities.Profile
import gr.cpaleop.dashboard.domain.entities.ProfileAcademicDetails
import gr.cpaleop.dashboard.domain.entities.ProfilePersonalDetails
import gr.cpaleop.dashboard.domain.entities.SocialMedia
import gr.cpaleop.dashboard.domain.repositories.PreferencesRepository

class ProfileMapper(private val preferencesRepository: PreferencesRepository) {

    suspend operator fun invoke(remoteProfile: RemoteProfile): Profile {
        @LanguageCode
        val preferredLanguage = preferencesRepository.getPreferredLanguage()

        val description: String
        val displayName: String
        val givenName: String
        val surname: String

        when (preferredLanguage) {
            LanguageCode.GREEK -> {
                description = remoteProfile.descriptionEl.safe()
                displayName = remoteProfile.displayNameEl.safe()
                givenName = remoteProfile.givenNameEl.safe()
                surname = remoteProfile.snEl.safe()
            }
            LanguageCode.ENGLISH -> {
                description = remoteProfile.description.safe()
                displayName = remoteProfile.displayName.safe()
                givenName = remoteProfile.givenName.safe()
                surname = remoteProfile.sn.safe()
            }
            else -> {
                description = ""
                displayName = ""
                givenName = ""
                surname = ""
            }
        }

        val personalDetails = ProfilePersonalDetails(
            lastName = surname,
            givenName = givenName,
            websiteUrl = remoteProfile.labeledURI.safe(),
            description = description,
            profileImageUrl = /*remoteProfile.profilePhoto.safe()*/ "https://pbs.twimg.com/profile_images/712048543670730753/D24VsCTN_400x400.jpg",
            telephoneNumber = remoteProfile.telephoneNumber.safe()
        )

        val academidDetails = ProfileAcademicDetails(
            am = remoteProfile.am.safe(),
            type = remoteProfile.eduPersonAffiliation.safe(),
            username = remoteProfile.username.safe(),
            displayName = displayName,
            currentSemester = remoteProfile.sem.safe(),
            registeredYear = remoteProfile.regyear.safe()
        )

        return Profile(
            personalDetails = personalDetails,
            academicDetails = academidDetails,
            socialMedia = SocialMedia(
                github = remoteProfile.socialMedia?.github.safe(),
                facebook = remoteProfile.socialMedia?.facebook.safe(),
                googlePlus = remoteProfile.socialMedia?.googlePlus.safe(),
                linkedIn = remoteProfile.socialMedia?.linkedIn.safe(),
                twitter = remoteProfile.socialMedia?.twitter.safe()
            )
        )
    }
}