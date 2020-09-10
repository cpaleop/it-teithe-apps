package gr.cpaleop.common.extensions

import android.content.Context
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView

fun ViewGroup?.hideKeyboard() {
    (this?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)
        ?.hideSoftInputFromWindow(this?.windowToken, 0)
}

fun TextView?.hideKeyboard() =
    (this?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)
        ?.hideSoftInputFromWindow(this?.windowToken, 0)


fun TextView?.openKeyboard() {
    val imm = this?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}