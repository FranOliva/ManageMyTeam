package es.us.managemyteam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.R
import es.us.managemyteam.databinding.FragmentEventsBinding
import es.us.managemyteam.extension.setNavIcon
import es.us.managemyteam.extension.setToolbarTitle
import es.us.managemyteam.extension.show


class EventsFragment : BaseFragment<FragmentEventsBinding>(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapView = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapView.getMapAsync(this)
/*
        val myRef = getDatabase().getReference(DatabaseTables.CLUB_TABLE)

        myRef.setValue(
            Club(
                "Azahar C.F",
                "14/10/2014"
            )
        )
*/

    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEventsBinding {
        return FragmentEventsBinding.inflate(inflater, container, false)
    }

    override fun setupToolbar(toolbar: Toolbar) {
        toolbar.apply {
            setToolbarTitle(getString(R.string.events))
            setNavIcon(null)
            show()
        }
    }

    override fun setupBottomBar(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.show()
    }

    override fun onMapReady(p0: GoogleMap?) {
        p0?.let {
            map = it
            val sydney = LatLng((-34).toDouble(), 151.toDouble())
            it.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"));
            map.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        }

    }
}