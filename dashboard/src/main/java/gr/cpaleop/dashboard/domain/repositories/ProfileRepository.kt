package gr.cpaleop.dashboard.domain.repositories

import gr.cpaleop.dashboard.domain.entities.Profile

interface ProfileRepository {

    suspend fun getProfile(): Profile
}