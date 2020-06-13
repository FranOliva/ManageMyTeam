package es.us.managemyteam.extension

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StyleableRes
import androidx.appcompat.app.AppCompatActivity
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

fun View.getBaseActivity(): AppCompatActivity? {
    return context as? AppCompatActivity
}

fun View.hideKeyboard() {
    context?.let {
        val imm = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
        clearFocus()
    }
}