package gr.cpaleop.profile.data

import gr.cpaleop.common.extensions.orEmpty
import gr.cpaleop.core.domain.DateFormatter
import gr.cpaleop.core.domain.behavior.LanguageCode
import gr.cpaleop.profile.data.model.remote.RemoteProfile
import gr.cpaleop.profile.domain.entities.AcademicDetails
import gr.cpaleop.profile.domain.entities.PersonalDetails
import gr.cpaleop.profile.domain.entities.Profile
import gr.cpaleop.profile.domain.entities.SocialMedia
import gr.cpaleop.profile.domain.repositories.PreferencesRepository

class ProfileMapper(
    private val preferencesRepository: PreferencesRepository,
    private val dateFormatter: DateFormatter
) {

    suspend operator fun invoke(remoteProfile: RemoteProfile): Profile {
        @LanguageCode
        val preferredLanguage = preferencesRepository.getPreferredLanguage()
        val givenName: String
        val lastName: String

        when (preferredLanguage) {
            LanguageCode.GREEK -> {
                givenName = remoteProfile.givenNameEl.orEmpty()
                lastName = remoteProfile.snEl.orEmpty()
            }
            LanguageCode.ENGLISH -> {
                givenName = remoteProfile.givenName.orEmpty()
                lastName = remoteProfile.sn.orEmpty()
            }
            else -> {
                givenName = ""
                lastName = ""
            }
        }

        val personalDetails = PersonalDetails(
            lastName = lastName,
            givenName = givenName,
            websiteUrl = remoteProfile.labeledURI.orEmpty(),
            description = remoteProfile.description.orEmpty(),
            profileImageUrl = remoteProfile.profilePhoto,
            telephoneNumber = remoteProfile.telephoneNumber.orEmpty(),
            email = remoteProfile.mail ?: remoteProfile.secondarymail.orEmpty(),
        )

        val academidDetails = AcademicDetails(
            am = remoteProfile.am.orEmpty(),
            type = remoteProfile.eduPersonAffiliation.orEmpty(),
            username = remoteProfile.username.orEmpty(),
            displayName = remoteProfile.displayName.orEmpty(),
            currentSemester = remoteProfile.sem.orEmpty(),
            registeredYear = remoteProfile.regyear.orEmpty()
        )

        val passwordTime = dateFormatter.getLocalTimestampFromUtc(
            remoteProfile.pwdChangedTime ?: ""
        )

        return Profile(
            personalDetails = personalDetails,
            academicDetails = academidDetails,
            passwordLastChangedTime = passwordTime,
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