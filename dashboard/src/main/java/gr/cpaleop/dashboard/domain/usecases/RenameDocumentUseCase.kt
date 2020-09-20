package gr.cpaleop.dashboard.domain.usecases

interface RenameDocumentUseCase {

    suspend operator fun invoke(documentUri: String, newName: String)
}