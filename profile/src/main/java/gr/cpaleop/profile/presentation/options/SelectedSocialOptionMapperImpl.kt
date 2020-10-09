package gr.cpaleop.profile.presentation.options

import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.profile.presentation.ProfileSocialDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SelectedSocialOptionMapperImpl(
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
) : SelectedSocialOptionMapper {

    override suspend fun invoke(profileSocialDetails: ProfileSocialDetails): SelectedSocialOption =
        withContext(defaultDispatcher) {
            SelectedSocialOption(
                profileSocialDetails.socialType,
                profileSocialDetails.labelRes,
                profileSocialDetails.value
            )
        }
}