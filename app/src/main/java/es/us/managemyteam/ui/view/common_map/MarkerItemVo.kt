package es.us.managemyteam.ui.view.common_map

import android.graphics.drawable.Drawable
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import es.us.managemyteam.extension.toBitmapDescriptor

data class MarkerItemVo(
    val id: String = "",
    val position: LatLng,
    val icon: Drawable? = null,
    val title: String? = null,
    val subtitle: String? = null
    ) {

    fun getGoogleMapMarker(): MarkerOptions {
        return MarkerOptions()
            .position(position)
            .icon(icon?.toBitmapDescriptor())
    }

}