package gr.cpaleop.dashboard.presentation.documents

import android.content.Context
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.domain.DateFormatter
import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.dashboard.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class FileDocumentMapper(
    private val applicationContext: Context,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    private val dateFormatter: DateFormatter
) {

    suspend operator fun invoke(document: Document): FileDocument =
        withContext(defaultDispatcher) {
            val lastModifiedHumanReadableFormat =
                dateFormatter.fileFormat(document.lastModified, "dd-MM-yy HH:mm")

            val type = when {
                document.type.contains("pdf") -> R.drawable.ic_pdf
                document.type.contains("image") -> R.drawable.ic_image
                document.type.contains("folder") -> R.drawable.ic_folder
                document.type.contains("docx") -> R.drawable.ic_docx
                document.type.contains("rar") -> R.drawable.ic_rar
                document.type.contains("zip") -> R.drawable.ic_zip
                else -> {
                    Timber.e("Unknown document file type: ${document.type}")
                    R.drawable.ic_document
                }
            }

            FileDocument(
                uri = document.uri,
                absolutePath = document.absolutePath,
                name = document.name,
                size = document.size,
                previewDrawable = type,
                lastModifiedDate = applicationContext.getString(
                    R.string.documents_modified,
                    lastModifiedHumanReadableFormat
                )
            )
        }
}