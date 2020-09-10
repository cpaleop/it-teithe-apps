package gr.cpaleop.dashboard.presentation

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

class OnCompoundDrawableClickListener(
    private val compoundDrawablePosition: Int,
    private val compoundDrawableCallback: () -> Unit
) : View.OnTouchListener {

    companion object {
        const val DRAWABLE_LEFT = 0
        const val DRAWABLE_TOP = 1
        const val DRAWABLE_RIGHT = 2
        const val DRAWABLE_BOTTOM = 3
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {

        if (view !is TextView) {
            throw IllegalArgumentException("View is not a subclass of TextView")
        }

        if (compoundDrawablePosition !in 0..3) {
            throw IllegalArgumentException("Invalid drawable position. Must be from 0 to 3")
        }

        if (event.action == MotionEvent.ACTION_UP) {
            val compoundDrawable = view.compoundDrawables[compoundDrawablePosition]

            val rightCalc = view.right - compoundDrawable.bounds.width() - view.paddingRight
            val leftCalc = view.left + compoundDrawable.bounds.width() + view.paddingLeft
            val bottomCalc = view.bottom - compoundDrawable.bounds.height() - view.paddingBottom
            val topCalc = compoundDrawable.bounds.height() - view.paddingTop

            val touchedRequestedDrawable = when (compoundDrawablePosition) {
                DRAWABLE_RIGHT -> event.rawX >= rightCalc
                DRAWABLE_LEFT -> event.rawX <= leftCalc
                DRAWABLE_BOTTOM -> event.rawY >= bottomCalc
                DRAWABLE_TOP -> event.rawY <= topCalc
                else -> false
            }

            if (compoundDrawable != null && touchedRequestedDrawable) {
                compoundDrawableCallback()
            }
            return false
        }
        return false
    }

    interface CompoundDrawableCallback {

        fun onCompoundDrawableClicked()
    }
}