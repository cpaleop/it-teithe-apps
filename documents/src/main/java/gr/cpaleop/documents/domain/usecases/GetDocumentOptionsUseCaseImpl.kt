package gr.cpaleop.documents.domain.usecases

import gr.cpaleop.documents.domain.entities.DocumentOptionType

class GetDocumentOptionsUseCaseImpl : GetDocumentOptionsUseCase {

    override fun invoke(): List<DocumentOptionType> {
        return listOf(
            DocumentOptionType.ANNOUNCEMENT,
            DocumentOptionType.RENAME,
            DocumentOptionType.DELETE,
            DocumentOptionType.SHARE
        )
    }
}