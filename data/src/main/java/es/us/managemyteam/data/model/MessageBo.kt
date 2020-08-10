package es.us.managemyteam.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class MessageBo(
    val message: String = "",
    val from: String = "",
    val date: Date = Date()
) : BaseBo(), Parcelable