package es.us.managemyteam.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ClubBo(
    val name: String? = null,
    val dateFundation: Date? = null,
    val location: String? = null,
    val president: String? = null,
    val coach: String? = null,
    val phoneNumber: String? = null,
    val mail: String? = null,
    val web: String? = null
) : BaseBo(), Parcelable