package es.us.managemyteam.extension

import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StyleableRes
import androidx.core.content.res.ResourcesCompat

const val NO_STYLE = -1

fun View.getColor(colorRes: Int) = ResourcesCompat.getColor(resources, colorRes, null)

fun View.getStyleTypeArray(
    attrs: AttributeSet?,
    @StyleableRes styleId: IntArray
): TypedArray {
    return context.obtainStyledAttributes(
        attrs,
        styleId,
        0,
        0
    )
}