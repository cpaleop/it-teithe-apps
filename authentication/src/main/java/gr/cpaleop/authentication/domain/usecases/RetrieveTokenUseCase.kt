package gr.cpaleop.authentication.domain.usecases

interface RetrieveTokenUseCase {

    suspend operator fun invoke(code: String?)
}