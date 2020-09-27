package gr.cpaleop.profile.domain.repositories

import gr.cpaleop.profile.domain.entities.Profile
import gr.cpaleop.profile.domain.entities.Social

interface ProfileRepository {

    suspend fun getProfile(): Profile

    suspend fun updateSocial(social: Social, value: String)
}