package gr.cpaleop.dashboard.presentation.profile.options

import gr.cpaleop.dashboard.presentation.profile.ProfileSocialDetails

class SelectedSocialOptionMapperImpl : SelectedSocialOptionMapper {

    override fun invoke(profileSocialDetails: ProfileSocialDetails): SelectedSocialOption {
        return SelectedSocialOption(
            profileSocialDetails.socialType,
            profileSocialDetails.label,
            profileSocialDetails.content
        )
    }
}