package es.us.managemyteam.ui.view.common_map

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng

interface MapListener {

    fun onMapClicked(latLng: LatLng)

    fun onMarkerClicked(markerClicked: MarkerItemVo)

    fun onMapReady()

    fun onLocationChanged(latLng: LatLng)

}

interface MapTransitionListener{

    fun onMapLoaded()

    fun onSnapshot(bitmap: Bitmap)

}