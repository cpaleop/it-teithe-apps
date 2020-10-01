package gr.cpaleop.profile.presentation

import gr.cpaleop.profile.domain.entities.Profile

interface ProfilePresentationMapper {

    suspend operator fun invoke(profile: Profile): ProfilePresentation
}