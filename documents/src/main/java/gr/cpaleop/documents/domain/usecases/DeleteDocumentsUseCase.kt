package gr.cpaleop.documents.domain.usecases

interface DeleteDocumentsUseCase {

    suspend operator fun invoke(documentUriList: List<String>)
}