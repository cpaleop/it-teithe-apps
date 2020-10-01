package gr.cpaleop.profile.presentation.options

import gr.cpaleop.profile.presentation.ProfileSocialDetails

class SelectedSocialOptionMapperImpl : SelectedSocialOptionMapper {

    override fun invoke(profileSocialDetails: ProfileSocialDetails): SelectedSocialOption {
        return SelectedSocialOption(
            profileSocialDetails.socialType,
            profileSocialDetails.label,
            profileSocialDetails.content
        )
    }
}