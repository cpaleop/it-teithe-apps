package gr.cpaleop.dashboard.domain.usecases

interface DeleteDocumentUseCase {

    suspend operator fun invoke(documentUri: String)
}