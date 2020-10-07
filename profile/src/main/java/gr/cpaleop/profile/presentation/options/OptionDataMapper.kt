package gr.cpaleop.profile.presentation.options

import gr.cpaleop.profile.presentation.ProfilePersonalDetails
import gr.cpaleop.profile.presentation.ProfileSocialDetails

class OptionDataMapper {

    operator fun invoke(profileSocialDetails: ProfileSocialDetails): OptionData {
        return OptionData(
            titleRes = profileSocialDetails.labelRes,
            value = profileSocialDetails.value
        )
    }

    operator fun invoke(profilePersonalDetails: ProfilePersonalDetails): OptionData {
        return OptionData(
            titleRes = profilePersonalDetails.label,
            value = profilePersonalDetails.value
        )
    }
}