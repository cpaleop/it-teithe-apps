package gr.cpaleop.profile.domain.usecases

import gr.cpaleop.profile.domain.repositories.PreferencesRepository

class GetPreferredThemeUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    GetPreferredThemeUseCase {

    override suspend fun invoke(): Int {
        return preferencesRepository.getPreferredTheme()
    }
}