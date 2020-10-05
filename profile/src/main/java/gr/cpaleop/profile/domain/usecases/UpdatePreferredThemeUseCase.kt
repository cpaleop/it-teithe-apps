package gr.cpaleop.profile.domain.usecases

interface UpdatePreferredThemeUseCase {

    suspend operator fun invoke(theme: Int)
}