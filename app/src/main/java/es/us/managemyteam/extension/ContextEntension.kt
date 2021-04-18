package es.us.managemyteam.extension

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import es.us.managemyteam.R
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

fun Context.showInformationDialog(
    message: String,
    dialogOnClickListener: DialogInterface.OnClickListener? = null
) {
    val dialog = AlertDialog.Builder(this)
        .setCancelable(true)
        .setMessage(message)
        .setTitle(null)
        .setPositiveButton(R.string.accept, dialogOnClickListener)
        .create()
    dialog.setOnShowListener {
        dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(this, R.color.black))
    }
    dialog.show()
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

