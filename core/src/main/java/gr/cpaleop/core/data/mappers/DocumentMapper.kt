package gr.cpaleop.core.data.mappers

import androidx.core.net.toUri
import gr.cpaleop.common.extensions.getMimeType
import gr.cpaleop.core.domain.entities.Document
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class DocumentMapper {

    suspend operator fun invoke(file: File, announcementId: String): Document =
        withContext(Dispatchers.IO) {
            val mimeType = file.getMimeType()
            Document(
                uri = file.toUri().toString(),
                announcementId = announcementId,
                absolutePath = file.absolutePath,
                name = file.name,
                type = mimeType,
                lastModified = file.lastModified(),
                previewUri = file.toURI().toASCIIString(),
                size = file.length()
            )
        }
}