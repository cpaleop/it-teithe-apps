package gr.cpaleop.core.presentation.share

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import gr.cpaleop.common.extensions.getMimeType
import gr.cpaleop.core.Authority
import java.io.File
import java.net.URI

/**
 * Convenient class to share files with other apps
 */
class FileShareImpl(@Authority private val authority: String) : FileShare {

    override operator fun invoke(context: Context, uriValue: String) {
        val file = File(URI(uriValue))
        val mimeType = file.getMimeType()
        val uri = FileProvider.getUriForFile(context, authority, file)
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = mimeType
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Send to"))
    }

    override operator fun invoke(context: Context, file: File) {
        val mimeType = file.getMimeType()
        val uri = FileProvider.getUriForFile(context, authority, file)
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = mimeType
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Send to"))
    }
}