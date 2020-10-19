package gr.cpaleop.documents.presentation.document

import gr.cpaleop.common.extensions.toResultSpannableString
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.domain.DateFormatter
import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.documents.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class FileDocumentMapper(
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    private val dateFormatter: DateFormatter
) {

    suspend operator fun invoke(document: Document, filterQuery: String = ""): FileDocument =
        withContext(defaultDispatcher) {
            val lastModifiedHumanReadableFormat =
                dateFormatter.fileFormat(document.lastModified, "dd-MM-yy HH:mm")

            val type = when {
                document.type.contains("pdf") -> R.drawable.layer_pdf
                document.type.contains("folder") -> R.drawable.layer_folder
                document.type.contains("doc") -> R.drawable.layer_docx
                document.type.contains("rar") -> R.drawable.layer_rar
                document.type.contains("zip") -> R.drawable.layer_zip
                document.type.contains("ppt") -> R.drawable.layer_ppt
                document.type.contains("jpg") ||
                        document.type.contains("jpeg") ||
                        document.type.contains("png") -> R.drawable.layer_image
                else -> {
                    Timber.e("Unknown document file type: ${document.type}")
                    R.drawable.layer_document
                }
            }

            val name = document.name.toResultSpannableString(filterQuery)
            FileDocument(
                uri = document.uri,
                absolutePath = document.absolutePath,
                name = name,
                size = document.size,
                previewDrawable = type,
                lastModifiedDate = LastModified(
                    R.string.documents_modified,
                    lastModifiedHumanReadableFormat
                )
            )
        }
}