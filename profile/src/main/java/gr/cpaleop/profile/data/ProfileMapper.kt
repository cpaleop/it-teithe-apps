package gr.cpaleop.profile.data

import gr.cpaleop.common.extensions.orEmpty
import gr.cpaleop.core.domain.behavior.LanguageCode
import gr.cpaleop.profile.data.model.remote.RemoteProfile
import gr.cpaleop.profile.domain.entities.AcademicDetails
import gr.cpaleop.profile.domain.entities.PersonalDetails
import gr.cpaleop.profile.domain.entities.Profile
import gr.cpaleop.profile.domain.entities.SocialMedia
import gr.cpaleop.profile.domain.repositories.PreferencesRepository

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
                description = remoteProfile.descriptionEl.orEmpty()
                displayName = remoteProfile.displayNameEl.orEmpty()
                givenName = remoteProfile.givenNameEl.orEmpty()
                surname = remoteProfile.snEl.orEmpty()
            }
            LanguageCode.ENGLISH -> {
                description = remoteProfile.description.orEmpty()
                displayName = remoteProfile.displayName.orEmpty()
                givenName = remoteProfile.givenName.orEmpty()
                surname = remoteProfile.sn.orEmpty()
            }
            else -> {
                description = ""
                displayName = ""
                givenName = ""
                surname = ""
            }
        }

        val personalDetails = PersonalDetails(
            lastName = surname,
            givenName = givenName,
            websiteUrl = remoteProfile.labeledURI.orEmpty(),
            description = description,
            profileImageUrl = remoteProfile.profilePhoto,
            telephoneNumber = remoteProfile.telephoneNumber.orEmpty(),
            email = remoteProfile.mail ?: remoteProfile.secondarymail.orEmpty(),
        )

        val academidDetails = AcademicDetails(
            am = remoteProfile.am.orEmpty(),
            type = remoteProfile.eduPersonAffiliation.orEmpty(),
            username = remoteProfile.username.orEmpty(),
            displayName = displayName,
            currentSemester = remoteProfile.sem.orEmpty(),
            registeredYear = remoteProfile.regyear.orEmpty()
        )

        return Profile(
            personalDetails = personalDetails,
            academicDetails = academidDetails,
            socialMedia = SocialMedia(
                github = remoteProfile.socialMedia?.github.orEmpty(),
                facebook = remoteProfile.socialMedia?.facebook.orEmpty(),
                googlePlus = remoteProfile.socialMedia?.googlePlus.orEmpty(),
                linkedIn = remoteProfile.socialMedia?.linkedIn.orEmpty(),
                twitter = remoteProfile.socialMedia?.twitter.orEmpty()
            )
        )
    }
}