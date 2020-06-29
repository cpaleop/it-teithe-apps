package gr.cpaleop.dashboard.data

import gr.cpaleop.dashboard.data.mappers.ProfileMapper
import gr.cpaleop.dashboard.data.remote.ProfileApi
import gr.cpaleop.dashboard.domain.entities.Profile
import gr.cpaleop.dashboard.domain.repositories.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepositoryImpl(
    private val profileApi: ProfileApi,
    private val profileMapper: ProfileMapper
) : ProfileRepository {

    override suspend fun getProfile(): Profile = withContext(Dispatchers.IO) {
        val remoteProfile = profileApi.fetchProfile()
        profileMapper(remoteProfile)
    }
}