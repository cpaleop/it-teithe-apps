package gr.cpaleop.profile.domain.usecases

import gr.cpaleop.profile.domain.entities.Profile
import gr.cpaleop.profile.domain.entities.Social

interface UpdateSocialUseCase {

    suspend operator fun invoke(social: Social, value: String): Profile
}