package gr.cpaleop.profile.domain.usecases

import gr.cpaleop.profile.domain.repositories.PreferencesRepository

class UpdatePreferredLanguageUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    UpdatePreferredLanguageUseCase {

    override suspend fun invoke(languageCode: String) {
        preferencesRepository.updatePreferredLanguage(languageCode)
    }
}