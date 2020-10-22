package gr.cpaleop.teithe_apps.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.Keep
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import gr.cpaleop.common.CompoundDrawableTouchListener
import gr.cpaleop.teithe_apps.R

class SearchEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    // Depicts the state of the drawable regardless of text state
    private var stateEmpty = true

    private var leftDrawable: Drawable? = null
    private val rightDrawable = AnimatedVectorDrawableCompat.create(
        context,
        R.drawable.search_to_cancel
    )

    private var leftDrawableListener: () -> Boolean = { false }
    private var rightDrawableListener: () -> Boolean = { false }

    init {
        this.setCompoundDrawablesWithIntrinsicBounds(
            leftDrawable,
            null,
            rightDrawable,
            null
        )

        doOnTextChanged { text, _, _, _ ->
            var animDrawable: AnimatedVectorDrawableCompat?
            if (text.isNullOrEmpty()) {
                if (!stateEmpty) {
                    (compoundDrawables[2] as Animatable2Compat).apply {
                        animDrawable = AnimatedVectorDrawableCompat.create(
                            context,
                            R.drawable.cancel_to_search
                        )
                        setCompoundDrawablesWithIntrinsicBounds(
                            leftDrawable,
                            null,
                            animDrawable,
                            null
                        )
                        (compoundDrawables[2] as AnimatedVectorDrawableCompat?)?.start()
                    }
                    stateEmpty = true
                }
            } else {
                if (stateEmpty) {
                    (compoundDrawables[2] as Animatable2Compat).apply {
                        animDrawable = AnimatedVectorDrawableCompat.create(
                            context,
                            R.drawable.search_to_cancel
                        )
                        setCompoundDrawablesWithIntrinsicBounds(
                            leftDrawable,
                            null,
                            animDrawable,
                            null
                        )
                        (compoundDrawables[2] as AnimatedVectorDrawableCompat?)?.start()
                        stateEmpty = false
                    }
                }
            }
        }
    }

    @Keep
    fun enableLeftDrawable(enable: Boolean) {
        leftDrawable = if (enable) {
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_left_arrow
            )
        } else {
            null
        }
        this.setCompoundDrawablesWithIntrinsicBounds(
            leftDrawable,
            null,
            rightDrawable,
            null
        )
    }

    @Keep
    fun setLeftDrawableListener(listener: () -> Boolean) {
        leftDrawableListener = listener
        setOnTouchListener(
            CompoundDrawableTouchListener(
                leftTouchListener = listener,
                rightTouchListener = rightDrawableListener
            )
        )
    }

    @Keep
    fun setRightDrawableListener(listener: () -> Boolean) {
        rightDrawableListener = listener
        setOnTouchListener(
            CompoundDrawableTouchListener(
                leftTouchListener = leftDrawableListener,
                rightTouchListener = listener
            )
        )
    }
}