package es.us.managemyteam.ui.view.common_map

import com.google.android.gms.maps.model.LatLng

interface MapListener {

    fun onMapClicked(latLng: LatLng)

    fun onMarkerClicked(markerClicked: MarkerItemVo)

    fun onMapReady()

    fun onLocationChanged(latLng: LatLng)

}