package gr.cpaleop.dashboard.presentation.documents.document

import androidx.annotation.DrawableRes

data class FileDocument(
    val uri: String,
    val absolutePath: String,
    val name: String,
    val size: Long,
    @DrawableRes
    val previewDrawable: Int,
    val lastModifiedDate: String
)