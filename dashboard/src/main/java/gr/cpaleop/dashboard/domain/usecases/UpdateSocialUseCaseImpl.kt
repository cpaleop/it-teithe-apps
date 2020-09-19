package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.Profile
import gr.cpaleop.dashboard.domain.entities.Social
import gr.cpaleop.dashboard.domain.repositories.ProfileRepository

class UpdateSocialUseCaseImpl(private val profileRepository: ProfileRepository) :
    UpdateSocialUseCase {

    override suspend fun invoke(social: Social, value: String): Profile {
        profileRepository.updateSocial(social, value)
        return profileRepository.getProfile()
    }
}