package gr.cpaleop.dashboard.presentation.files.options

import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.domain.entities.FileOptionType

class FileOptionMapper {

    operator fun invoke(fileOptionType: FileOptionType): FileOption {
        return when (fileOptionType) {
            FileOptionType.RENAME -> FileOption(
                FileOptionType.RENAME,
                R.string.files_option_rename,
                R.drawable.ic_edit
            )
            FileOptionType.DELETE -> FileOption(
                FileOptionType.DELETE,
                R.string.files_option_delete,
                R.drawable.ic_delete
            )
            FileOptionType.SHARE -> FileOption(
                FileOptionType.SHARE,
                R.string.files_option_share,
                R.drawable.ic_share
            )
            FileOptionType.INFO -> FileOption(
                FileOptionType.INFO,
                R.string.files_option_info,
                R.drawable.ic_info
            )
            FileOptionType.ANNOUNCEMENT -> FileOption(
                FileOptionType.ANNOUNCEMENT,
                R.string.files_option_announcement,
                R.drawable.ic_link
            )
        }
    }
}