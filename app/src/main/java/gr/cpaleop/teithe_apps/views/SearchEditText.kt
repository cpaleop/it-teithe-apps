package gr.cpaleop.teithe_apps.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.Keep
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.content.res.getDimensionOrThrow
import androidx.core.content.withStyledAttributes
import androidx.core.widget.doOnTextChanged
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.google.android.material.shape.MaterialShapeDrawable
import gr.cpaleop.common.CompoundDrawableTouchListener
import gr.cpaleop.common.extensions.toDp
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

    private var materialShapeDrawable: MaterialShapeDrawable? = null

    init {
        materialShapeDrawable = MaterialShapeDrawable(
            context, attrs, R.attr.searchEditTextStyle, R.style.Theme_Itteitheapps_SearchEditText
        ).apply {
            initializeElevationOverlay(context)
        }

        context.withStyledAttributes(
            attrs,
            R.styleable.SearchEditText,
            0,
            R.style.Theme_Itteitheapps_SearchEditText
        ) {
            val backgroundTint = getColorStateList(R.styleable.SearchEditText_backgroundTint)
            val elevation = getDimensionOrThrow(R.styleable.SearchEditText_android_elevation)
            val cornerSize = getDimension(R.styleable.SearchEditText_cornerSize, 8f.toDp())
            background = materialShapeDrawable
            backgroundTintList = backgroundTint
            setElevation(elevation)
            materialShapeDrawable?.setCornerSize(cornerSize)
        }

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

    override fun setElevation(elevation: Float) {
        super.setElevation(elevation)
        materialShapeDrawable?.elevation = elevation
    }

    @Keep
    fun setCornerSize(cornerSize: Float) {
        materialShapeDrawable?.setCornerSize(cornerSize)
        invalidate()
    }
}