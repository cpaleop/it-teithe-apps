package gr.cpaleop.profile.domain.usecases

import gr.cpaleop.core.domain.behavior.LanguageCode

interface UpdatePreferredLanguageUseCase {

    suspend operator fun invoke(@LanguageCode languageCode: String)
}