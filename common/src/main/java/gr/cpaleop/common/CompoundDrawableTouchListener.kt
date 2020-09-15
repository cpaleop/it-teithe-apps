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
            val leftCompoundDrawable = view.compoundDrawables[0] ?: return false
            val topCompoundDrawable = view.compoundDrawables[0] ?: return false
            val rightCompoundDrawable = view.compoundDrawables[0] ?: return false
            val bottomCompoundDrawable = view.compoundDrawables[0] ?: return false


            val rightCalc = view.right - rightCompoundDrawable.bounds.width() - view.paddingRight
            val leftCalc = view.left + leftCompoundDrawable.bounds.width() + view.paddingLeft
            val bottomCalc =
                view.bottom - bottomCompoundDrawable.bounds.height() - view.paddingBottom
            val topCalc = topCompoundDrawable.bounds.height() - view.paddingTop

            return when {
                event.rawX >= rightCalc -> rightTouchListener()
                event.rawX <= leftCalc -> leftTouchListener()
                event.rawY >= bottomCalc -> bottomTouchListener()
                event.rawY <= topCalc -> topTouchListener()
                else -> view.performClick()
            }
        }
        return view.performClick()
    }
}