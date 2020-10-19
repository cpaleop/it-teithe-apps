package gr.cpaleop.documents.presentation.document

import android.text.SpannableString
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class FileDocument(
    val uri: String,
    val absolutePath: String,
    val name: SpannableString,
    val size: Long,
    @DrawableRes
    val previewDrawable: Int,
    val lastModifiedDate: LastModified,
    val isSelected: Boolean = false
)

data class LastModified(
    @StringRes
    val labelRes: Int,
    val dateHumanReadable: String
)