package gr.cpaleop.dashboard.data

import gr.cpaleop.dashboard.data.mappers.ProfileMapper
import gr.cpaleop.dashboard.data.remote.ProfileApi
import gr.cpaleop.dashboard.domain.entities.Profile
import gr.cpaleop.dashboard.domain.entities.Social
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

    override suspend fun updateSocial(social: Social, value: String) = withContext(Dispatchers.IO) {
        when (social) {
            Social.GITHUB -> profileApi.updateProfile(github = value)
            Social.FACEBOOK -> profileApi.updateProfile(facebook = value)
            Social.TWITTER -> profileApi.updateProfile(twitter = value)
            Social.LINKEDIN -> profileApi.updateProfile(linkedIn = value)
            Social.GOOGLEPLUS -> profileApi.updateProfile(googlePlus = value)
        }
    }
}