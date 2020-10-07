package gr.cpaleop.documents.presentation.document

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class FileDocument(
    val uri: String,
    val absolutePath: String,
    val name: String,
    val size: Long,
    @DrawableRes
    val previewDrawable: Int,
    val lastModifiedDate: LastModified
)

data class LastModified(
    @StringRes
    val labelRes: Int,
    val dateHumanReadable: String
)