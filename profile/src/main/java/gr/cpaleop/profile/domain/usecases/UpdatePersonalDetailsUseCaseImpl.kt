package gr.cpaleop.profile.domain.usecases

import gr.cpaleop.profile.domain.entities.Personal
import gr.cpaleop.profile.domain.entities.Profile
import gr.cpaleop.profile.domain.repositories.ProfileRepository

class UpdatePersonalDetailsUseCaseImpl(private val profileRepository: ProfileRepository) :
    UpdatePersonalDetailsUseCase {

    override suspend fun invoke(personalType: Personal, value: String): Profile {
        profileRepository.updatePersonal(personalType, value)
        return profileRepository.getProfile()
    }
}