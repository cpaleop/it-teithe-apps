package gr.cpaleop.teithe_apps.views

import android.content.Context
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

    private var hasSearchViewAnimatedToCancel: Boolean = false
    private var hasSearchViewAnimatedToSearch: Boolean = false
    private var isLeftDrawableEnabled: Boolean = false

    private val endDrawable = AnimatedVectorDrawableCompat.create(
        context,
        R.drawable.search_to_cancel
    )

    private val startDrawable = ContextCompat.getDrawable(
        context,
        R.drawable.ic_left_arrow
    )

    private var leftDrawableListener: () -> Boolean = { false }
    private var rightDrawableListener: () -> Boolean = { false }

    init {
        this.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            endDrawable,
            null
        )

        doOnTextChanged { text, _, _, _ ->
            var animDrawable: AnimatedVectorDrawableCompat?
            if (text?.isEmpty() == true) {
                (compoundDrawables[2] as Animatable2Compat).apply {
                    if (!hasSearchViewAnimatedToSearch) {
                        animDrawable = AnimatedVectorDrawableCompat.create(
                            context,
                            R.drawable.cancel_to_search
                        )
                        if (isLeftDrawableEnabled) {
                            setCompoundDrawablesWithIntrinsicBounds(
                                startDrawable,
                                null,
                                animDrawable,
                                null
                            )
                        } else {
                            setCompoundDrawablesWithIntrinsicBounds(
                                null,
                                null,
                                animDrawable,
                                null
                            )
                        }

                        animDrawable?.start()
                        hasSearchViewAnimatedToCancel = false
                        hasSearchViewAnimatedToSearch = !hasSearchViewAnimatedToSearch
                    }
                }
            } else {
                (compoundDrawables[2] as Animatable2Compat).apply {
                    if (!hasSearchViewAnimatedToCancel) {
                        animDrawable = AnimatedVectorDrawableCompat.create(
                            context,
                            R.drawable.search_to_cancel
                        )
                        if (isLeftDrawableEnabled) {
                            setCompoundDrawablesWithIntrinsicBounds(
                                startDrawable,
                                null,
                                animDrawable,
                                null
                            )
                        } else {
                            setCompoundDrawablesWithIntrinsicBounds(
                                null,
                                null,
                                animDrawable,
                                null
                            )
                        }

                        animDrawable?.start()
                        hasSearchViewAnimatedToSearch = false
                        hasSearchViewAnimatedToCancel = !hasSearchViewAnimatedToCancel
                    }
                }
            }
        }
    }

    @Keep
    fun enableLeftDrawable(enable: Boolean) {
        isLeftDrawableEnabled = enable
        val backButton = if (enable) startDrawable else null
        this.setCompoundDrawablesWithIntrinsicBounds(
            backButton,
            null,
            endDrawable,
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