package gr.cpaleop.documents.presentation.announcement_folder

import gr.cpaleop.common.extensions.toResultSpannableString
import gr.cpaleop.documents.domain.entities.AnnouncementFolder

class AnnouncementFolderPresentationMapper {

    operator fun invoke(
        announcementFolder: AnnouncementFolder,
        filterQuery: String = ""
    ): AnnouncementFolderPresentation {
        val title = announcementFolder.title.toResultSpannableString(filterQuery)

        return AnnouncementFolderPresentation(
            id = announcementFolder.id,
            title = title
        )
    }
}