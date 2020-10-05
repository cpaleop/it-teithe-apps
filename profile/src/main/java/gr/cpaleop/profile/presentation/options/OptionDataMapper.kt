package gr.cpaleop.profile.presentation.options

import gr.cpaleop.profile.presentation.ProfilePersonalDetails
import gr.cpaleop.profile.presentation.ProfileSocialDetails

class OptionDataMapper {

    operator fun invoke(profileSocialDetails: ProfileSocialDetails): OptionData {
        return OptionData(
            title = profileSocialDetails.label,
            value = profileSocialDetails.value
        )
    }

    operator fun invoke(profilePersonalDetails: ProfilePersonalDetails): OptionData {
        return OptionData(
            title = profilePersonalDetails.label,
            value = profilePersonalDetails.value
        )
    }
}