package gr.cpaleop.dashboard.presentation.profile.options

import gr.cpaleop.dashboard.presentation.profile.ProfileSocialDetails

interface SelectedSocialOptionMapper {

    operator fun invoke(profileSocialDetails: ProfileSocialDetails): SelectedSocialOption
}