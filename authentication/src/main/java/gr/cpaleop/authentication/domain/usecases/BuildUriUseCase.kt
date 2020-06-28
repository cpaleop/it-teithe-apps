package gr.cpaleop.authentication.domain.usecases

interface BuildUriUseCase {

    operator fun invoke(loginUrl: String, clientId: String): String
}