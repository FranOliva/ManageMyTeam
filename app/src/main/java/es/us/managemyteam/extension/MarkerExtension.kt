package es.us.managemyteam.extension

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker

fun List<Marker>.getBounds(): LatLngBounds {
    val points = map { it.position }
    val builder = LatLngBounds.builder()

    points.forEach { builder.include(it) }

    return builder.build()
}


fun List<Marker>.getBoundsWithUserLocation(userLocation: LatLng?): LatLngBounds {
    val points = map { it.position }
    val builder = LatLngBounds.builder()

    points.forEach { builder.include(it) }
    userLocation?.let {
        builder.include(userLocation)
    }

    return builder.build()
}