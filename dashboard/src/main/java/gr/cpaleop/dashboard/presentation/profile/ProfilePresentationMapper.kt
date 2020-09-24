package gr.cpaleop.dashboard.presentation.profile

import gr.cpaleop.dashboard.domain.entities.Profile

interface ProfilePresentationMapper {

    suspend operator fun invoke(profile: Profile): ProfilePresentation
}