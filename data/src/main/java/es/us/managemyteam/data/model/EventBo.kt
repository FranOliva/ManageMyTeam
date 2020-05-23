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
    val assistants: List<UserBo>? = null,
    val eventType: EventType? = null
) : BaseBo(), Parcelable

@Parcelize
enum class EventType : Parcelable {
    TRAINING,
    MATCH,
    OTHER
}