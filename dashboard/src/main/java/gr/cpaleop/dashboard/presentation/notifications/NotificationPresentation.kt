package gr.cpaleop.dashboard.presentation.notifications

import android.text.SpannableString

data class NotificationPresentation(
    val id: String,
    val seen: Boolean,
    val date: String,
    val title: SpannableString,
    val category: String,
    val publisherName: SpannableString
)