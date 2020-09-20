package gr.cpaleop.dashboard.presentation.files.options

import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.domain.entities.DocumentOptionType

class DocumentOptionMapper {

    operator fun invoke(documentOptionType: DocumentOptionType): DocumentOption {
        return when (documentOptionType) {
            DocumentOptionType.RENAME -> DocumentOption(
                DocumentOptionType.RENAME,
                R.string.files_option_rename,
                R.drawable.ic_edit
            )
            DocumentOptionType.DELETE -> DocumentOption(
                DocumentOptionType.DELETE,
                R.string.files_option_delete,
                R.drawable.ic_delete
            )
            DocumentOptionType.SHARE -> DocumentOption(
                DocumentOptionType.SHARE,
                R.string.files_option_share,
                R.drawable.ic_share
            )
            DocumentOptionType.ANNOUNCEMENT -> DocumentOption(
                DocumentOptionType.ANNOUNCEMENT,
                R.string.files_option_announcement,
                R.drawable.ic_link
            )
        }
    }
}