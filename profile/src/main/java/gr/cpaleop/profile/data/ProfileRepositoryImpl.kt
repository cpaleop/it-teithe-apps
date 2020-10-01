package gr.cpaleop.profile.data

import gr.cpaleop.profile.data.remote.ProfileApi
import gr.cpaleop.profile.domain.entities.Profile
import gr.cpaleop.profile.domain.entities.Social
import gr.cpaleop.profile.domain.repositories.ProfileRepository
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