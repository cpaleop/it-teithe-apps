package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.Profile

interface GetProfileUseCase {

    suspend operator fun invoke(): Profile
}