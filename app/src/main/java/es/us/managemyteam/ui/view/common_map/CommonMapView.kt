package es.us.managemyteam.ui.view.common_map

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import es.us.managemyteam.R
import es.us.managemyteam.databinding.ViewMapBinding
import es.us.managemyteam.extension.getBounds
import es.us.managemyteam.extension.getBoundsWithUserLocation
import es.us.managemyteam.extension.toBitmapDescriptor
import es.us.managemyteam.manager.LocationManager
import kotlin.math.roundToInt

private const val DEFAULT_ZOOM = 15f
private const val NO_ZOOM = 0f

class CommonMapView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), OnMapReadyCallback,
    GoogleMap.OnMapClickListener,
    GoogleMap.OnMarkerClickListener {

    private var currentLocationMarker: Marker? = null
    private val viewBinding = ViewMapBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
    var map: GoogleMap? = null
    private var mapListener: MapListener? = null
    private var mapTransitionListener: MapTransitionListener? = null
    private val locationManager = LocationManager(context)
    private var currentLocation: LatLng? = null
    private val markers = hashMapOf<Marker, MarkerItemVo>()
    private var currentColor = ContextCompat.getColor(context, R.color.primary)
    private var showUserPosition = true
    private var interactionEnabled = true
    private var liteMode = false
    private var animateCamera = true
    private var permissionWasAsked = false

    fun onResume() {
        try {
            viewBinding.mapViewMainMap.onResume()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun onPause() {
        try {
            viewBinding.mapViewMainMap.onPause()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun onStart() {
        try {
            viewBinding.mapViewMainMap.onStart()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun onStop() {
        try {
            viewBinding.mapViewMainMap.onStop()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun onLowMemory() {
        try {
            viewBinding.mapViewMainMap.onLowMemory()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun onDestroy() {
        viewBinding.mapViewMainMap.onDestroy()
    }

    fun enableLiteMode(enable: Boolean) {
        liteMode = enable
    }

    fun setShowUserPosition(show: Boolean) {
        showUserPosition = show
    }

    fun setEnableInteraction(enable: Boolean) {
        interactionEnabled = enable
        map?.uiSettings?.setAllGesturesEnabled(interactionEnabled)
    }

    fun onCreate(savedInstanceState: Bundle?) {
        viewBinding.mapViewMainMap.apply {
            this.onCreate(savedInstanceState)
            this.getMapAsync(this@CommonMapView)
        }
        viewBinding.mapContainerLocation.setOnClickListener {
            clickOnCurrentLocation()
        }
    }

    fun initialize(fragment: Fragment) {
        if (isMapReady()) {
            processWithPermissions(fragment)
        }
    }

    fun addMarker(vararg marker: MarkerItemVo) {
        addMarker(animateCamera, *marker)
    }

    fun onRequestPermissionResult(fragment: Fragment, permissionsGranted: IntArray) {
        if (locationManager.checkGrantedPermissions(permissionsGranted)) {
            updateCurrentLocation(fragment)
        } else {
            permissionWasAsked = true
        }
    }

    fun setMapTransitionListener(transitionListener: MapTransitionListener) {
        this.mapTransitionListener = transitionListener
    }

    fun setMapListener(mapListener: MapListener?) {
        this.mapListener = mapListener
        if (isMapReady()) { // Notify if map is already initialized
            this.mapListener?.onMapReady()
        }
    }

    fun animateCamera(animate: Boolean) {
        animateCamera = animate
    }

    fun isMapReady(): Boolean {
        return map != null
    }

    fun showLocationButton(show: Boolean) {
        viewBinding.mapContainerLocation.visibility = if (show) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    fun showMarkers(animateCamera: Boolean = false) {
        map?.let {
            currentLocation?.let {
                mapListener?.onLocationChanged(it)
                if (showUserPosition) {
                    currentLocationMarker?.remove()
                    currentLocationMarker =
                        map?.addMarker(getCurrentPositionMarker(it))
                }
            }
            addMarker(animateCamera, *markers.values.toTypedArray())
        }
    }

    fun clearMarkers(justFromMap: Boolean = false) {
        if (!justFromMap) {
            markers.clear()
        }
        map?.clear()
        if (showUserPosition) {
            showCurrentLocation()
        }
    }

    fun isEmpty(): Boolean {
        return markers.isEmpty()
    }

    fun getAddressFromMarkerPosition(latLng: LatLng): String? {
        return locationManager.getAddressOfLatLng(latLng)
    }

    fun getMarkerPositionFromAddress(address: String): LatLng? {
        return locationManager.getLatLngOfAddress(address)
    }

    fun showMarkerInfo(markerItem: MarkerItemVo) {
        markers.keys.find { it.position == markerItem.position }?.showInfoWindow()
    }

    private fun addMarker(animate: Boolean, vararg marker: MarkerItemVo) {
        map?.let {
            marker.forEach { current ->
                val googleMarker = it.addMarker(current.getGoogleMapMarker())
                markers[googleMarker] = current
            }
            if (markers.size > 1) {
                if (showUserPosition) {
                    applyZoom(
                        markers.keys.toList().getBoundsWithUserLocation(currentLocation),
                        animate
                    )
                } else {
                    applyZoom(markers.keys.toList().getBounds(), animate)
                }
            } else if (markers.size == 1) {
                applyZoom(markers.values.first().position)
            }
            updateInfoWindowAdapter()
        }
    }

    private fun shouldZoom(zoom: Float): Boolean {
        return !(zoom == DEFAULT_ZOOM &&
                (map?.cameraPosition?.zoom ?: NO_ZOOM) > DEFAULT_ZOOM)
    }

    private fun applyZoom(
        latLng: LatLng,
        animate: Boolean = animateCamera,
        zoom: Float = DEFAULT_ZOOM
    ) {
        val zoomToUse = if (shouldZoom(zoom)) {
            zoom
        } else {
            (map?.cameraPosition?.zoom ?: NO_ZOOM)
        }
        if (animate) {
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomToUse))
        } else {
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomToUse))
        }
    }

    private fun applyZoom(
        bounds: LatLngBounds,
        animate: Boolean = animateCamera,
        zoom: Float = DEFAULT_ZOOM
    ) {
        try {
            val padding = (measuredWidth * 0.20).roundToInt()
            if (animate) {
                map?.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        bounds,
                        measuredWidth,
                        measuredHeight,
                        padding
                    )
                )
            } else {
                map?.moveCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        bounds,
                        measuredWidth,
                        measuredHeight,
                        padding
                    )
                )
            }
        } catch (ex: Exception) {
            Log.e(javaClass.simpleName, ex.message)
        }
    }

    private fun processWithPermissions(fragment: Fragment) {
        if (locationManager.checkNeedPermissions(context)) {
            if (!permissionWasAsked) {
                locationManager.requestPermission(fragment)
            }
        } else {
            updateCurrentLocation(fragment)
        }
    }

    private fun showCurrentLocation(shouldAnimateCamera: Boolean = animateCamera) {
        val currentLocation = currentLocation
        currentLocation?.let {
            mapListener?.onLocationChanged(it)
            if (showUserPosition) {
                currentLocationMarker?.remove()
                currentLocationMarker = map?.addMarker(getCurrentPositionMarker(currentLocation))
                applyZoom(currentLocation, shouldAnimateCamera)

            }
        }
    }

    fun updateCurrentLocation(lifecycleOwner: LifecycleOwner, animate: Boolean = true) {
        currentLocation = locationManager.getCurrentLocation()
        locationManager.getCurrentLocationData().observe(lifecycleOwner,
            Observer<LatLng> {
                currentLocation = it
                showCurrentLocation(animate)
            })
        showCurrentLocation(animate)
    }

    fun snapshot() {
        map?.snapshot {
            mapTransitionListener?.onSnapshot(it)
        }
    }

    private fun getCurrentPositionMarker(position: LatLng): MarkerOptions {
        return MarkerOptions()
            .position(position)
            .icon(
                MapStyleUtil.getCurrentPositionDrawable(
                    context,
                    currentColor
                )?.toBitmapDescriptor()
            )
            .draggable(false)
            .visible(true)
    }

    private fun updateInfoWindowAdapter() {
        map?.setInfoWindowAdapter(MapInfoWindowAdapter(context, markers.values))
    }

    //region GoogleMap

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.apply {
            val googleMapOptions = GoogleMapOptions().liteMode(liteMode)
            mapType = googleMapOptions.mapType
        }
        map = googleMap
        map?.apply {
            uiSettings.isMapToolbarEnabled = false
            uiSettings.setAllGesturesEnabled(interactionEnabled)
            setOnMapClickListener(this@CommonMapView)
            setOnMarkerClickListener(this@CommonMapView)
            setOnMapLoadedCallback {
                mapTransitionListener?.onMapLoaded()
            }
        }
        mapListener?.onMapReady()
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        marker?.let {
            if (!liteMode) {
                applyZoom(it.position, true, DEFAULT_ZOOM)
            }
            markers[it]?.let { vo ->
                mapListener?.onMarkerClicked(vo)
            }
        }

        return true
    }

    override fun onMapClick(latlng: LatLng?) {
        latlng?.let {
            mapListener?.onMapClicked(it)
        }
    }

    //endregion

    //region clicks

    private fun clickOnCurrentLocation() {
        currentLocation?.let {
            applyZoom(it, true, DEFAULT_ZOOM)
        }
    }

    fun getBounds() =
        map?.cameraPosition?.target


    //endregion

}