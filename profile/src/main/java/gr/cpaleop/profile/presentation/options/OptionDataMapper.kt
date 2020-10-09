package gr.cpaleop.profile.presentation.options

import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.profile.presentation.ProfilePersonalDetails
import gr.cpaleop.profile.presentation.ProfileSocialDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class OptionDataMapper(
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke(profileSocialDetails: ProfileSocialDetails): OptionData =
        withContext(defaultDispatcher) {
            OptionData(
                titleRes = profileSocialDetails.labelRes,
                value = profileSocialDetails.value
            )
        }

    suspend operator fun invoke(profilePersonalDetails: ProfilePersonalDetails): OptionData =
        withContext(defaultDispatcher) {
            OptionData(
                titleRes = profilePersonalDetails.label,
                value = profilePersonalDetails.value
            )
        }
}