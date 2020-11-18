package es.us.managemyteam.extension

import android.util.Patterns

fun String.isEmail(): Boolean {
    return (isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches())
}

fun String.isURL(): Boolean {
    return (isNotBlank() && Patterns.WEB_URL.matcher(this).matches())
}

fun String.isPhone(): Boolean {
    return (isNotBlank() && Patterns.PHONE.matcher(this).matches())
}