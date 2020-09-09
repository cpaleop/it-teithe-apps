package gr.cpaleop.core.data.mappers

import android.webkit.MimeTypeMap
import gr.cpaleop.core.domain.entities.Document
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class DocumentMapper {

    suspend operator fun invoke(file: File): Document = withContext(Dispatchers.IO) {
        val mimeType = MimeTypeMap.getFileExtensionFromUrl(file.toURI().toASCIIString())
        Document(
            absolutePath = file.absolutePath,
            name = file.name,
            type = mimeType,
            lastModified = file.lastModified(),
            previewUri = file.toURI().toASCIIString(),
            size = file.length()
        )
    }
}