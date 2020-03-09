package es.us.managemyteam.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClubBo(
    val name: String? = null,
    val dateFundation: String? = null,
    val location: String? = null,
    val president: String? = null,
    val coach: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val web: String? = null,
    val image: String? = null
) : Parcelable