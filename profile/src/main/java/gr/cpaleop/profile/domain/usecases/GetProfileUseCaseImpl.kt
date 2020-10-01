package gr.cpaleop.profile.domain.usecases

import gr.cpaleop.profile.domain.entities.Profile
import gr.cpaleop.profile.domain.repositories.ProfileRepository

class GetProfileUseCaseImpl(private val profileRepository: ProfileRepository) : GetProfileUseCase {

    override suspend fun invoke(): Profile {
        return profileRepository.getProfile()
    }
}