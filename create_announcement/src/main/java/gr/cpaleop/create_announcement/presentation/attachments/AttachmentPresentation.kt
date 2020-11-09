package gr.cpaleop.create_announcement.presentation.attachments

import androidx.annotation.DrawableRes

data class AttachmentPresentation(
    val uri: String,
    val name: String,
    @DrawableRes
    val typeDrawableRes: Int
)