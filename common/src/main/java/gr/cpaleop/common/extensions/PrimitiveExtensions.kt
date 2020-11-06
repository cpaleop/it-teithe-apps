package gr.cpaleop.common.extensions

import android.util.DisplayMetrics
import android.util.TypedValue

/**
 * Applies density dimensions to the [Int]
 */
fun Int.toDp(displayMetrics: DisplayMetrics = DisplayMetrics()) =
    toFloat().toDp(displayMetrics).toInt()

/**
 * Applies density dimensions to the [Float]
 */
fun Float.toDp(displayMetrics: DisplayMetrics = DisplayMetrics()) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, displayMetrics)