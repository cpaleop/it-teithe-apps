package gr.cpaleop.create_announcement.presentation.attachments

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import gr.cpaleop.core.dispatchers.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import gr.cpaleop.teithe_apps.R as appR

class AttachmentPresentationMapper(
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val applicationContext: Context
) {

    suspend operator fun invoke(uriValue: String): AttachmentPresentation =
        withContext(ioDispatcher) {
            val uri = Uri.parse(uriValue)
            val mimeType = applicationContext.contentResolver.getType(uri) ?: ""
            var name = ""

            applicationContext.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    name = cursor.getString(nameIndex)
                }
            }

            val typeDrawableRes = when {
                mimeType.contains("pdf") -> appR.drawable.ic_pdf
                mimeType.contains("folder") -> appR.drawable.ic_folder
                mimeType.contains("doc") -> appR.drawable.ic_docx
                mimeType.contains("rar") -> appR.drawable.ic_rar
                mimeType.contains("zip") -> appR.drawable.ic_zip
                mimeType.contains("ppt") -> appR.drawable.ic_ppt
                mimeType.contains("jpg") ||
                        mimeType.contains("jpeg") ||
                        mimeType.contains("png") -> appR.drawable.ic_image
                else -> {
                    Timber.e("Unknown document file type: ${mimeType}")
                    appR.drawable.ic_document
                }
            }

            return@withContext AttachmentPresentation(
                uri = uriValue,
                name = name,
                typeDrawableRes = typeDrawableRes
            )
        }
}