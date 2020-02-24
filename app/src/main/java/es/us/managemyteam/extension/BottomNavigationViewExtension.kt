package es.us.managemyteam.extension

import android.view.View.GONE
import android.view.View.VISIBLE
import com.google.android.material.bottomnavigation.BottomNavigationView

fun BottomNavigationView.hide() {
    visibility = GONE
}

fun BottomNavigationView.show() {
    visibility = VISIBLE
}