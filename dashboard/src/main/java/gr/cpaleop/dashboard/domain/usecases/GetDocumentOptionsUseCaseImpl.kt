package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.DocumentOptionType

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