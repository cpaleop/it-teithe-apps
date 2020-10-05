package gr.cpaleop.profile.domain.usecases

import gr.cpaleop.profile.domain.entities.Personal
import gr.cpaleop.profile.domain.entities.Profile

interface UpdatePersonalDetailsUseCase {

    suspend operator fun invoke(personalType: Personal, value: String): Profile
}