package gr.cpaleop.profile.domain.usecases

interface GetPreferredThemeUseCase {

    suspend operator fun invoke(): Int
}