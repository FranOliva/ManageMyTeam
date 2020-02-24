package es.us.managemyteam.extension

import android.graphics.drawable.Drawable
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.widget.Toolbar

fun Toolbar.setNavAction(listener: (View) -> Unit) {
    setNavigationOnClickListener(listener)
}

fun Toolbar.setNavIcon(iconDrawable: Drawable?) {
    navigationIcon = iconDrawable
}

fun Toolbar.show() {
    visibility = VISIBLE
}

fun Toolbar.hide() {
    visibility = GONE
}

fun Toolbar.centerTitle(center: Boolean) {
    // TODO: center text
}