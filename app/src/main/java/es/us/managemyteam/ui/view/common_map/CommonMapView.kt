package es.us.managemyteam.ui.view.common_map

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import es.us.managemyteam.databinding.ViewMapBinding

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
    private val locationManager = es.us.managemyteam.manager.LocationManager(context)
    private var currentLocation: LatLng? = null
    private val markers = hashMapOf<Marker, MarkerItemVo>()
    private var showUserPosition = true
    private var animateCamera = true

    override fun onMapReady(p0: GoogleMap?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMapClick(p0: LatLng?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}