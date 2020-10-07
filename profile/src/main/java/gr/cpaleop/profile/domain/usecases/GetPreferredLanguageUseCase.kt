package gr.cpaleop.profile.domain.usecases

import gr.cpaleop.core.domain.behavior.LanguageCode

interface GetPreferredLanguageUseCase {

    @LanguageCode
    suspend operator fun invoke(): String
}