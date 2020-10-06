package gr.cpaleop.profile.domain.usecases

import gr.cpaleop.profile.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class ObservePreferredThemeUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    ObservePreferredThemeUseCase {

    override fun invoke(): Flow<Int> {
        return preferencesRepository.getPreferredThemeFlow()
    }
}