package gr.cpaleop.common.extensions

import android.webkit.MimeTypeMap
import java.io.File
import java.util.*

fun File.getMimeType(): String {
    val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "*/*"
    return mimeType.toLowerCase(Locale.getDefault())
}