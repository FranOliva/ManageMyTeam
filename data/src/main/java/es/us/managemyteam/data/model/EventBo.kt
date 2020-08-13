package es.us.managemyteam.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class EventBo(
    val title: String? = null,
    val date: Date? = null,
    val location: LocationBo? = null,
    val description: String? = null,
    var call: CallBo? = null,
    val eventType: String? = null
) : BaseBo(), Parcelable
