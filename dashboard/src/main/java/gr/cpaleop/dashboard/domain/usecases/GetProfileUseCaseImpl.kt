package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.Profile
import gr.cpaleop.dashboard.domain.repositories.ProfileRepository

class GetProfileUseCaseImpl(private val profileRepository: ProfileRepository) : GetProfileUseCase {

    override suspend fun invoke(): Profile {
        return profileRepository.getProfile()
    }
}