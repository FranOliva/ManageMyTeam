package es.us.managemyteam.extension

import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng

private const val MAX_RESULTS = 10

fun Geocoder.getLatLngOfAddress(address: String): LatLng? {
    return try {
        val results = getFromLocationName(address, MAX_RESULTS)

        results.firstOrNull()?.let {
            LatLng(it.latitude, it.longitude)
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }
}

fun Geocoder.getAddressOfLatLng(latLng: LatLng?): String? {
    return latLng?.let {
        try {
            val results = getFromLocation(latLng.latitude, latLng.longitude, MAX_RESULTS)
            results.firstOrNull()?.getAddressLine(0)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }
}