package gr.cpaleop.dashboard.presentation.files

import android.content.Context
import gr.cpaleop.core.domain.DateFormatter
import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.dashboard.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FileDocumentMapper(
    private val applicationContext: Context,
    private val dateFormatter: DateFormatter
) {

    suspend operator fun invoke(document: Document): FileDocument =
        withContext(Dispatchers.Default) {
            val lastModifiedHumanReadableFormat =
                dateFormatter.fileFormat(document.lastModified, "dd-MM-yy HH:mm")

            val type = when {
                document.type.contains("pdf") -> R.drawable.ic_pdf
                document.type.contains("image") -> R.drawable.ic_image
                else -> R.drawable.ic_document
            }

            FileDocument(
                absolutePath = document.absolutePath,
                name = document.name,
                size = document.size,
                previewDrawable = type,
                lastModifiedDate = applicationContext.getString(
                    R.string.files_modified,
                    lastModifiedHumanReadableFormat
                )
            )
        }
}