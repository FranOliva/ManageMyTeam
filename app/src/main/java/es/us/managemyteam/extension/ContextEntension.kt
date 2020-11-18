package es.us.managemyteam.extension

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import com.google.android.gms.maps.model.LatLng
import java.util.*

private const val GOOGLE_MAPS_URL = "http://maps.google.com/maps?q="
private const val PREFIX_PHONE = "tel:"
private const val CALENDAR_ID = 3

fun Context.startUrlActionView(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}

fun Context.startGoogleMapsByLatLng(latLng: LatLng) {
    startUrlActionView(GOOGLE_MAPS_URL + latLng.toGoogleMapsFormat())
}

@SuppressLint("MissingPermission")
fun Context.startCalendarEvent(title: String?, dateInit: Long?) {
    val uri = contentResolver.insert(
        CalendarContract.Events.CONTENT_URI,
        getContentValues(title, dateInit)
    )
    startActivity(Intent(Intent.ACTION_VIEW).setData(uri))
}

private fun getContentValues(
    title: String?,
    dateIni: Long?
): ContentValues {
    val values = ContentValues()
    dateIni?.let {
        values.put(CalendarContract.Events.DTSTART, dateIni)
        values.put(CalendarContract.Events.DTEND, dateIni)
    }
    values.put(CalendarContract.Events.TITLE, title ?: "")
    values.put(CalendarContract.Events.CALENDAR_ID, CALENDAR_ID)
    values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().displayName)

    return values
}