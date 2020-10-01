package gr.cpaleop.documents.domain.usecases

interface GetDocumentOptionsUseCase {

    operator fun invoke(): List<gr.cpaleop.documents.domain.entities.DocumentOptionType>
}