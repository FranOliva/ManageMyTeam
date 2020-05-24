package es.us.managemyteam.extension

import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import es.us.managemyteam.R
import es.us.managemyteam.ui.view.common_map.MapView
import es.us.managemyteam.ui.view.common_map.MarkerItemVo

fun Location.toLatLng() = LatLng(latitude, longitude)

fun LatLng.getMarkerItemVo(mapView: MapView) = MarkerItemVo(
    position = this,
    icon = ContextCompat.getDrawable(mapView.context, R.drawable.ic_poi),
    title = mapView.getAddressFromMarkerPosition(this)
)
