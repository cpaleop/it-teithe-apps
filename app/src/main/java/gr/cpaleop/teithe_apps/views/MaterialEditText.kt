package gr.cpaleop.teithe_apps.views

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Keep
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.getDimensionOrThrow
import androidx.core.content.withStyledAttributes
import com.google.android.material.shape.MaterialShapeDrawable
import gr.cpaleop.common.extensions.toDp
import gr.cpaleop.teithe_apps.R

open class MaterialEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

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
    }

    @Keep
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