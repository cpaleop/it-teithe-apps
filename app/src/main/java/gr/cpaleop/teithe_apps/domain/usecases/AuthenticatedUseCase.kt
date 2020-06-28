package gr.cpaleop.teithe_apps.domain.usecases

interface AuthenticatedUseCase {

    suspend operator fun invoke(): Boolean
}