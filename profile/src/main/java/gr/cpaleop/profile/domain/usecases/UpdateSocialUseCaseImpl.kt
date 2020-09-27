package gr.cpaleop.profile.domain.usecases

import gr.cpaleop.profile.domain.entities.Profile
import gr.cpaleop.profile.domain.entities.Social
import gr.cpaleop.profile.domain.repositories.ProfileRepository

class UpdateSocialUseCaseImpl(private val profileRepository: ProfileRepository) :
    UpdateSocialUseCase {

    override suspend fun invoke(social: Social, value: String): Profile {
        profileRepository.updateSocial(social, value)
        return profileRepository.getProfile()
    }
}