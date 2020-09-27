package gr.cpaleop.documents.domain.usecases

interface RenameDocumentUseCase {

    suspend operator fun invoke(documentUri: String, newName: String)
}