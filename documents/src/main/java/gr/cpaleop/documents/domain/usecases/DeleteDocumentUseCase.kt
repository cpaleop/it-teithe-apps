package gr.cpaleop.documents.domain.usecases

interface DeleteDocumentUseCase {

    suspend operator fun invoke(documentUri: String)
}