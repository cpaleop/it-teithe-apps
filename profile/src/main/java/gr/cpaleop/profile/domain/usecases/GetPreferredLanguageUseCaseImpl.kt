package gr.cpaleop.profile.domain.usecases

import gr.cpaleop.profile.domain.repositories.PreferencesRepository

class GetPreferredLanguageUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    GetPreferredLanguageUseCase {

    override suspend fun invoke(): String {
        return preferencesRepository.getPreferredLanguage()
    }
}