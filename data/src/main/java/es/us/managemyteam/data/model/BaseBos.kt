package es.us.managemyteam.data.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

open class BaseBo(
    val id: String? = null
)

@Parcelize
data class LocationBo(
    val location: LatLng? = null,
    val address: String? = null
) : Parcelable