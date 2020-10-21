package gr.cpaleop.common.extensions

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible

fun ViewGroup?.hideKeyboard() {
    (this?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)
        ?.hideSoftInputFromWindow(this?.windowToken, 0)
}

fun View.animateVisibiltyWithScale(
    shouldShow: Boolean,
    duration: Long = 200
): ViewPropertyAnimator {
    val alpha = if (shouldShow) 1f else 0f
    val scale = if (shouldShow) 1f else 1.1f

    val animator = this.animate()
        .setDuration(duration)
        .alpha(alpha)
        .scaleY(scale)
        .scaleX(scale)

    if (shouldShow) {
        animator.setStartListener {
            this.isVisible = shouldShow
        }
    } else {
        animator.setEndListener {
            this.isVisible = shouldShow
        }
    }

    return animator
}