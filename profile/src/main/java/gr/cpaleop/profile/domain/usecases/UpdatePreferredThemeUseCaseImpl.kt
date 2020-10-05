package gr.cpaleop.profile.domain.usecases

import gr.cpaleop.profile.domain.repositories.PreferencesRepository

class UpdatePreferredThemeUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    UpdatePreferredThemeUseCase {

    override suspend fun invoke(theme: Int) {
        preferencesRepository.updatePreferredTheme(theme)
    }
}