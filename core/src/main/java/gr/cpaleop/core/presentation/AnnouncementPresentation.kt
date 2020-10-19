package gr.cpaleop.core.presentation

import android.text.SpannableString

data class AnnouncementPresentation(
    val id: String,
    val date: String,
    val title: SpannableString,
    val content: SpannableString,
    val publisherName: SpannableString,
    val category: SpannableString,
    val hasAttachments: Boolean
)