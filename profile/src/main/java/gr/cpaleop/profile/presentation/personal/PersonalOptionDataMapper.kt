package gr.cpaleop.profile.presentation.personal

import gr.cpaleop.profile.presentation.ProfilePersonalDetails

class PersonalOptionDataMapper {

    operator fun invoke(profilePersonalDetails: ProfilePersonalDetails): PersonalOptionData {
        return PersonalOptionData(
            type = profilePersonalDetails.type,
            label = profilePersonalDetails.label,
            value = profilePersonalDetails.value
        )
    }
}