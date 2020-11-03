package gr.cpaleop.announcement.domain.usecases

interface ToggleFavoriteUseCase {

    suspend operator fun invoke(id: String)
}