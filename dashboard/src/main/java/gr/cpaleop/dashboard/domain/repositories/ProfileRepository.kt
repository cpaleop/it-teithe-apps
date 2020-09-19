package gr.cpaleop.dashboard.domain.repositories

import gr.cpaleop.dashboard.domain.entities.Profile
import gr.cpaleop.dashboard.domain.entities.Social

interface ProfileRepository {

    suspend fun getProfile(): Profile

    suspend fun updateSocial(social: Social, value: String)
}