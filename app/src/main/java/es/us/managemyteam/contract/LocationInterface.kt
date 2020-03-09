package es.us.managemyteam.contract

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.google.android.gms.maps.model.LatLng

interface LocationInterface {

    fun checkGrantedPermissions(permissions: IntArray): Boolean

    fun checkNeedPermissions(context: Context): Boolean

    fun requestPermission(fragment: Fragment)

    fun getCurrentLocation(): LatLng?

    fun getCurrentLocationData(): LiveData<LatLng>

    fun onCreate(context: Context)

    fun onStart(context: Context)

    fun onStop()

    fun getAddressOfLatLng(latLng: LatLng): String?

    fun getLatLngOfAddress(address: String): LatLng?

}