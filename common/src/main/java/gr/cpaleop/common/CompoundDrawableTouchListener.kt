package gr.cpaleop.common

import android.view.MotionEvent
import android.view.View
import android.widget.TextView

class CompoundDrawableTouchListener(
    private val rightTouchListener: () -> Boolean = { false },
    private val leftTouchListener: () -> Boolean = { false },
    private val topTouchListener: () -> Boolean = { false },
    private val bottomTouchListener: () -> Boolean = { false }
) : View.OnTouchListener {

    override fun onTouch(view: View, event: MotionEvent): Boolean {

        if (view !is TextView) {
            throw IllegalArgumentException("View is not a subclass of TextView")
        }

        if (event.action == MotionEvent.ACTION_DOWN) {
            val leftCompoundDrawable = view.compoundDrawables[0] ?: null
            val topCompoundDrawable = view.compoundDrawables[1] ?: null
            val rightCompoundDrawable = view.compoundDrawables[2] ?: null
            val bottomCompoundDrawable = view.compoundDrawables[3] ?: null

            val rightCalc = if (rightCompoundDrawable != null) {
                view.right - rightCompoundDrawable.bounds.width() - view.paddingRight
            } else {
                null
            }
            val leftCalc = if (leftCompoundDrawable != null) {
                view.left + leftCompoundDrawable.bounds.width() + view.paddingLeft
            } else {
                null
            }
            val bottomCalc = if (bottomCompoundDrawable != null) {
                view.bottom - bottomCompoundDrawable.bounds.height() - view.paddingBottom
            } else {
                null
            }

            val topCalc = if (topCompoundDrawable != null) {
                topCompoundDrawable.bounds.height() - view.paddingTop
            } else {
                null
            }

            return when {
                rightCalc != null && event.rawX >= rightCalc -> rightTouchListener()
                leftCalc != null && event.rawX <= leftCalc -> leftTouchListener()
                bottomCalc != null && event.rawY >= bottomCalc -> bottomTouchListener()
                topCalc != null && event.rawY <= topCalc -> topTouchListener()
                else -> view.performClick()
            }
        }
        return view.performClick()
    }
}