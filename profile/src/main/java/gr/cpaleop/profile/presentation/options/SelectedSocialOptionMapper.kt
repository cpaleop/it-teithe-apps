package gr.cpaleop.profile.presentation.options

import gr.cpaleop.profile.presentation.ProfileSocialDetails

interface SelectedSocialOptionMapper {

    operator fun invoke(profileSocialDetails: ProfileSocialDetails): SelectedSocialOption
}