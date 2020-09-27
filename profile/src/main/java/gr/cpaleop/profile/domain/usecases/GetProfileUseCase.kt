package gr.cpaleop.profile.domain.usecases

import gr.cpaleop.profile.domain.entities.Profile

interface GetProfileUseCase {

    suspend operator fun invoke(): Profile
}