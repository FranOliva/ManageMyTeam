package es.us.managemyteam.extension

import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.widget.Toolbar

fun Toolbar.setNavAction(listener: (View) -> Unit) {
    setNavigationOnClickListener(listener)
}

fun Toolbar.setNavIcon(iconDrawable: Drawable?) {
    navigationIcon = iconDrawable
}