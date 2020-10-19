package gr.cpaleop.common.extensions

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan

fun String?.orEmpty(fallbackValue: String = ""): String {
    return if (this.isNullOrEmpty()) fallbackValue
    else this
}

fun String?.toResultSpannableString(filterQuery: String): SpannableString {
    val start = this?.indexOf(filterQuery, ignoreCase = true) ?: return SpannableString("")
    return SpannableString(this).apply {
        if (start >= 0) {
            setSpan(
                BackgroundColorSpan(Color.YELLOW),
                start,
                start + filterQuery.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(Color.BLACK),
                start,
                start + filterQuery.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
}