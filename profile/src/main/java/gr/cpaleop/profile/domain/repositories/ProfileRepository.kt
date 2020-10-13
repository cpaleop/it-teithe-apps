package gr.cpaleop.profile.domain.repositories

import gr.cpaleop.profile.domain.entities.Personal
import gr.cpaleop.profile.domain.entities.Profile
import gr.cpaleop.profile.domain.entities.Social

interface ProfileRepository {

    suspend fun getProfile(): Profile

    suspend fun updateSocial(social: Social, value: String)

    suspend fun updatePersonal(type: Personal, value: String)

    suspend fun changePassword(oldPassword: String, newPassword: String)
}