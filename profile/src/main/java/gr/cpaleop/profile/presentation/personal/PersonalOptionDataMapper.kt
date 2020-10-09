package gr.cpaleop.profile.presentation.personal

import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.profile.presentation.ProfilePersonalDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PersonalOptionDataMapper(@DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher) {

    suspend operator fun invoke(profilePersonalDetails: ProfilePersonalDetails): PersonalOptionData =
        withContext(defaultDispatcher) {
            PersonalOptionData(
                type = profilePersonalDetails.type,
                labelRes = profilePersonalDetails.label,
                value = profilePersonalDetails.value
            )
        }
}