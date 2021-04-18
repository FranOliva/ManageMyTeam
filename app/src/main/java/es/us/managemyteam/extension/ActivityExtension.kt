package es.us.managemyteam.extension

import androidx.fragment.app.FragmentActivity

fun FragmentActivity?.safeUse(function: () -> Unit) {
    if (this != null && !isFinishing) {
        function.invoke()
    }
}