package gr.cpaleop.common.extensions

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import gr.cpaleop.common.GreekLanguageIntonationHelper

fun String?.orEmpty(fallbackValue: String = ""): String {
    return if (this.isNullOrEmpty()) fallbackValue
    else this
}

fun String?.toResultSpannableString(filterQuery: String): SpannableString {
    val start = this?.removeIntonation()?.indexOf(filterQuery.removeIntonation(), ignoreCase = true)
        ?: return SpannableString("")
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

fun String.removeIntonation(): String {
    var toBeReturned = ""
    this.forEach {
        toBeReturned += GreekLanguageIntonationHelper.normalizeTonnes(it)
    }
    return toBeReturned
}