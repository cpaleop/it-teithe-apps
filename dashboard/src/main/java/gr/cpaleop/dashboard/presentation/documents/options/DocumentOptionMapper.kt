package gr.cpaleop.dashboard.presentation.documents.options

import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.domain.entities.DocumentOptionType

class DocumentOptionMapper {

    operator fun invoke(documentOptionType: DocumentOptionType): DocumentOption {
        return when (documentOptionType) {
            DocumentOptionType.RENAME -> DocumentOption(
                DocumentOptionType.RENAME,
                R.string.documents_option_rename,
                R.drawable.ic_edit
            )
            DocumentOptionType.DELETE -> DocumentOption(
                DocumentOptionType.DELETE,
                R.string.documents_option_delete,
                R.drawable.ic_delete
            )
            DocumentOptionType.SHARE -> DocumentOption(
                DocumentOptionType.SHARE,
                R.string.documents_option_share,
                R.drawable.ic_share
            )
            DocumentOptionType.ANNOUNCEMENT -> DocumentOption(
                DocumentOptionType.ANNOUNCEMENT,
                R.string.documents_option_announcement,
                R.drawable.ic_link
            )
        }
    }
}