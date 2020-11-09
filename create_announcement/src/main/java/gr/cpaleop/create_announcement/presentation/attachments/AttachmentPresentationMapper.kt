package gr.cpaleop.create_announcement.presentation.attachments

import gr.cpaleop.create_announcement.domain.entities.Attachment
import timber.log.Timber
import gr.cpaleop.teithe_apps.R as appR

class AttachmentPresentationMapper {

    operator fun invoke(attachment: Attachment): AttachmentPresentation {
        val typeDrawableRes = when {
            attachment.type.contains("pdf") -> appR.drawable.ic_pdf
            attachment.type.contains("folder") -> appR.drawable.ic_folder
            attachment.type.contains("doc") -> appR.drawable.ic_docx
            attachment.type.contains("rar") -> appR.drawable.ic_rar
            attachment.type.contains("zip") -> appR.drawable.ic_zip
            attachment.type.contains("ppt") -> appR.drawable.ic_ppt
            attachment.type.contains("jpg") ||
                    attachment.type.contains("jpeg") ||
                    attachment.type.contains("png") -> appR.drawable.ic_image
            else -> {
                Timber.e("Unknown document file type: ${attachment.type}")
                appR.drawable.ic_document
            }
        }

        return AttachmentPresentation(
            uri = attachment.uri,
            name = attachment.name,
            typeDrawableRes = typeDrawableRes
        )
    }
}