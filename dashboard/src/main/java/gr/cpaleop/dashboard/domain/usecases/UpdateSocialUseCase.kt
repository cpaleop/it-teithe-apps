package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.Profile
import gr.cpaleop.dashboard.domain.entities.Social

interface UpdateSocialUseCase {

    suspend operator fun invoke(social: Social, value: String): Profile
}