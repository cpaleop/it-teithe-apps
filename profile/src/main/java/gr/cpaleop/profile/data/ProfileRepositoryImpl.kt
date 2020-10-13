package gr.cpaleop.profile.data

import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.profile.data.remote.ProfileApi
import gr.cpaleop.profile.domain.entities.Personal
import gr.cpaleop.profile.domain.entities.Profile
import gr.cpaleop.profile.domain.entities.Social
import gr.cpaleop.profile.domain.repositories.ProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ProfileRepositoryImpl(
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val profileApi: ProfileApi,
    private val profileMapper: ProfileMapper
) : ProfileRepository {

    override suspend fun getProfile(): Profile = withContext(ioDispatcher) {
        val remoteProfile = profileApi.fetchProfile()
        profileMapper(remoteProfile)
    }

    override suspend fun updateSocial(social: Social, value: String) = withContext(ioDispatcher) {
        when (social) {
            Social.GITHUB -> profileApi.updateProfile(github = value)
            Social.FACEBOOK -> profileApi.updateProfile(facebook = value)
            Social.TWITTER -> profileApi.updateProfile(twitter = value)
            Social.LINKEDIN -> profileApi.updateProfile(linkedIn = value)
            Social.GOOGLEPLUS -> profileApi.updateProfile(googlePlus = value)
        }
    }

    override suspend fun updatePersonal(type: Personal, value: String) =
        withContext(ioDispatcher) {
            when (type) {
                Personal.DISPLAY_NAME -> profileApi.updateProfile(displayName = value)
                Personal.DESCRIPTION -> profileApi.updateProfile(description = value)
                Personal.MAIL -> profileApi.updateProfile(mail = value)
                Personal.TELEPHONE_NUMBER -> profileApi.updateProfile(telephoneNumber = value)
            }
        }

    override suspend fun changePassword(oldPassword: String, newPassword: String) =
        withContext(ioDispatcher) {
            profileApi.updatePassword(oldPassword, newPassword)
        }
}