package gr.cpaleop.create_announcement.data

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.create_announcement.domain.entities.Attachment
import gr.cpaleop.create_announcement.domain.repositories.AttachmentsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class AttachmentsRepositoryImpl(
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val applicationContext: Context
) : AttachmentsRepository {

    private var attachments = mutableListOf<Attachment>()

    override suspend fun clearSelections(uriList: List<String>): Unit = withContext(ioDispatcher) {
        if (uriList.isEmpty()) {
            attachments = mutableListOf()
        } else {
            val attachment = attachments.filter { uriList.contains(it.uri) }
            attachments.removeAll(attachment)
        }
    }

    override suspend fun addAttachments(uriList: List<String>) {
        uriList.forEach { uriString ->
            //Skip if it already exists
            if (attachments.map { it.uri }.contains(uriString)) return

            val uri = Uri.parse(uriString)
            val mimeType = applicationContext.contentResolver.getType(uri) ?: ""

            applicationContext.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    Timber.e("NAME INDEX: $nameIndex")
                    val name = cursor.getString(nameIndex)

                    val attachment = Attachment(
                        uri = uriString,
                        name = name,
                        type = mimeType
                    )
                    attachments.add(attachment)
                }
            }
        }
    }

    override fun getAttachments(): List<Attachment> {
        return attachments.toList()
    }
}